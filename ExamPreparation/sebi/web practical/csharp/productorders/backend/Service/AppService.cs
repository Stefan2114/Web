using System.Text.Json;
using System.Text.Json.Serialization;
using Microsoft.EntityFrameworkCore;

public class AppService
{
    private readonly AppDbContext _context;

    public AppService(AppDbContext context)
    {
        _context = context;
    }

    public async Task<List<Product>> GetProductsStartingWithStringAsync(string startingWith)
    {
        return await _context.Products.Where(product =>
            product.Name.StartsWith(startingWith))
            .ToListAsync();
    }

    public async Task<(bool Success, string Message, int? orderId)> BuyProductWithQuantityAsync(string username, string productName, int quantity)
    {
        var product = await _context.Products.FirstOrDefaultAsync(p => p.Name == productName);

        if (product == null) return (false, "Product does not exist", null);

        var order = new Order
        {
            User = username,
            ProductId = product.Id,
            Quantity = quantity
        };

        _context.Orders.Add(order);

        await _context.SaveChangesAsync();

        return (true, "Successfully saved order", order.Id);
    }

    public async Task<(bool Success, string Message)> CancelOrders(string orderIdsString)
    {
        var orderIds = orderIdsString.Split(",").ToList();

        var orders = await _context.Orders.ToListAsync();

        orders.RemoveAll(r => orderIds.Contains(r.Id.ToString()));

        await _context.SaveChangesAsync();

        return (true, "Successfully cancelled orders");
    }
}