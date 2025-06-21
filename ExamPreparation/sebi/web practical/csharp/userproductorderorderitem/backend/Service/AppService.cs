using System.Text.Json;
using Microsoft.EntityFrameworkCore;

public class AppService
{
    private readonly AppDbContext _context;

    public AppService(AppDbContext context)
    {
        _context = context;
    }

    public async Task<List<Product>> GetProductsAsync()
    {
        return await _context.Products.ToListAsync();
    }

    private async Task<HashSet<string>> GetCategorySet(Order order)
    {
        var result = new HashSet<string>();

        var orderItems = await _context.OrderItems.Where(oi => oi.OrderId == order.Id).ToListAsync();
        foreach (var orderItem in orderItems)
        {
            var product = await _context.Products.FirstOrDefaultAsync(p => p.Id == orderItem.ProductId);
            result.Add(product!.Name.Split("-")[0]);
        }

        return result;
    }

    private async Task<bool> CheckIfDiversified(string username, Product product)
    {
        var user = await _context.Users.FirstOrDefaultAsync(u => u.Username == username);

        var orders = await _context.Orders.Where(o => o.UserId == user!.Id).ToListAsync();

        orders = orders.TakeLast(3).ToList();

        if (orders.Count < 3) return true;
        
        var categoriesOrder1 = await GetCategorySet(orders.ElementAt(0));
        var categoriesOrder2 = await GetCategorySet(orders.ElementAt(1));
        var categoriesOrder3 = await GetCategorySet(orders.ElementAt(2));

        var productCategory = product.Name.Split("-")[0];

        return categoriesOrder1.Contains(productCategory) && categoriesOrder2.Contains(productCategory) && categoriesOrder3.Contains(productCategory);
    }

    public async Task<(bool Success, bool isDiversified, Product product)> SelectProductAsync(string username, int productId)
    {
        var product = await _context.Products.FirstOrDefaultAsync(p => p.Id == productId)!;

        var isDiversified = await CheckIfDiversified(username, product!);

        return (true, !isDiversified, product!);
    }

    public class LowerCaseNamingPolicy : JsonNamingPolicy
    {
        public override string ConvertName(string name)
        {
            if (string.IsNullOrEmpty(name) || !char.IsUpper(name[0]))
                return name;
            return name.ToLower();
        }
    }

    private List<Product> DeserializeProducts(string json)
    {
        if (string.IsNullOrEmpty(json) || json.Trim() == "[]" || json.Trim() == "null")
            return new List<Product>();

        try
        {
            var options = new JsonSerializerOptions { PropertyNamingPolicy = new LowerCaseNamingPolicy() };
            return JsonSerializer.Deserialize<List<Product>>(json, options) ?? new List<Product>();
        }
        catch (JsonException)
        {
            return new List<Product>();
        }
    }

    private bool CheckIfSharedCategory(List<Product> products)
    {
        HashSet<string> categories = new HashSet<string>();

        foreach (var product in products)
        {
            var parts = product.Name.Split("-");
            if (categories.Contains(parts[0])) return true;

            categories.Add(parts[0]);
        }

        return false;
    }

    private double GetTotalPrice(List<Product> products)
    {
        double finalPrice = 0;
        foreach (var product in products)
        {
            finalPrice += product.Price;
        }

        if (products.Count >= 3)
        {
            finalPrice -= finalPrice * 0.1;
        }

        if (CheckIfSharedCategory(products))
        {
            finalPrice -= finalPrice * 0.05;
        }

        return finalPrice;
    }

    public async Task<(bool success, string message, double finalPrice)> ConfirmOrder(string username, string productListJson)
    {
        var user = await _context.Users.FirstOrDefaultAsync(u => u.Username == username);

        var productList = DeserializeProducts(productListJson);

        var totalPrice = GetTotalPrice(productList);

        var order = new Order
        {
            UserId = user!.Id,
            TotalPrice = totalPrice
        };

        _context.Orders.Add(order);
        await _context.SaveChangesAsync();

        foreach (var product in productList)
        {
            var orderItem = new OrderItem
            {
                OrderId = order.Id,
                ProductId = product.Id
            };

            _context.OrderItems.Add(orderItem);
        }

        await _context.SaveChangesAsync();

        return (true, "Successfully confirmed order", totalPrice);
    }
}