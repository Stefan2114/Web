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
    public async Task<IActionResult> GetProducts()
    {
        var products = await _service.GetProductsAsync();
        return Ok(products);
    }

    [HttpGet("selectProduct")]
    public async Task<IActionResult> SelectProduct(string username, int productId)
    {
        var (success, isDiversified, product) = await _service.SelectProductAsync(username, productId);

        if (success)
        {
            return Ok(new { success = true, isDiversified, product });
        }
        else
        {
            return BadRequest(new { success = false });
        }
    }

    [HttpPost("confirmOrder")]
    public async Task<IActionResult> ConfirmOrder([FromBody] OrderConfirmationRequest request)
    {
        var (success, message, finalPrice) = await _service.ConfirmOrder(
            request.Username!,
            request.ProductListJson!);

        if (success)
        {
            return Ok(new { success = true, message, finalPrice });
        }
        else
        {
            return BadRequest(new { success = false, message });
        }
    }
}