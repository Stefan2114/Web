using Microsoft.AspNetCore.Mvc;

[Route("api")]
[ApiController]
public class AppController : ControllerBase
{
    private readonly AppService _service;

    public AppController(AppService service)
    {
        _service = service;
    }

    [HttpGet("products")]
    public async Task<IActionResult> GetProductsStartingWith([FromForm] string startingWith)
    {
        var products = await _service.GetProductsStartingWithStringAsync(startingWith);
        return Ok(products);
    }

    [HttpPost("placeOrder")]
    public async Task<IActionResult> PlaceOrder([FromForm] OrderRequest request)
    {
        var (success, message, orderId) = await _service.BuyProductWithQuantityAsync(
            request.User!,
            request.ProductName!,
            request.Quantity!);

        if (success)
        {
            // Return HTML redirect instead of JSON
            var html = $@"
                <html>
                <head>
                    <script>
                        // Add order ID to sessionStorage
                        const currentOrders = sessionStorage.getItem('orders') || '';
                        const newOrders = currentOrders ? currentOrders + ',' + {orderId} : '{orderId}';
                        sessionStorage.setItem('orders', newOrders);
                        
                        // Redirect back to order page
                        window.location.href = '/order.html?success=true&orderId={orderId}';
                    </script>
                </head>
                <body>
                    <p>Order placed successfully! Redirecting...</p>
                </body>
                </html>";
            return Content(html, "text/html");
        }
        else
        {
            var html = $@"
                <html>
                <head>
                    <script>
                        window.location.href = '/order.html?success=false&message={message}';
                    </script>
                </head>
                <body>
                    <p>Error: {message}</p>
                </body>
                </html>";
            return Content(html, "text/html");
        }
    }

    [HttpPost("cancelOrders")]
    public async Task<IActionResult> CancelOrders([FromForm] string orderIdsString)
    {
        var (success, message) = await _service.CancelOrders(
            orderIdsString);

        if (success)
        {
            var html = $@"
                <html>
                <head>
                    <script>
                        sessionStorage.setItem('orders', '');
                        window.location.href = '/order.html?cancelled=true';
                    </script>
                </head>
                <body>
                    <p>Orders cancelled successfully! Redirecting...</p>
                </body>
                </html>";
            return Content(html, "text/html");
        }
        else
        {
            var html = $@"
                <html>
                <head>
                    <script>
                        window.location.href = '/order.html?error={message}';
                    </script>
                </head>
                <body>
                    <p>Error: {message}</p>
                </body>
                </html>";
            return Content(html, "text/html");
        }
    }
}