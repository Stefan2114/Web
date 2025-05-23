using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

[ApiController]
[Route("api/recipes")]
public class RecipeController : ControllerBase
{
    private readonly AppDbContext _context;

    public RecipeController(AppDbContext context) => _context = context;

    [HttpGet]
    public async Task<IActionResult> GetAll()
    {
        try
        {
            var recipes = await _context.Recipes.OrderBy(r => r.Name).ToListAsync();
            return Ok(recipes);
        }
        catch (Exception ex)
        {
            return StatusCode(500, new { error = "Failed to retrieve recipes", details = ex.Message });
        }
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetById(int id)
    {
        try
        {
            if (id <= 0)
            {
                return BadRequest(new { error = "Invalid recipe ID" });
            }

            var recipe = await _context.Recipes.FindAsync(id);
            return recipe != null ? Ok(recipe) : NotFound(new { error = "Recipe not found" });
        }
        catch (Exception ex)
        {
            return StatusCode(500, new { error = "Failed to retrieve recipe", details = ex.Message });
        }
    }

    [HttpGet("filter")]
    public async Task<IActionResult> Filter([FromQuery] string? type)
    {
        try
        {
            var query = _context.Recipes.AsQueryable();

            if (!string.IsNullOrWhiteSpace(type))
            {
                query = query.Where(r => r.Type.ToLower().Contains(type.ToLower()));
            }

            var recipes = await query.OrderBy(r => r.Name).ToListAsync();
            return Ok(recipes);
        }
        catch (Exception ex)
        {
            return StatusCode(500, new { error = "Failed to filter recipes", details = ex.Message });
        }
    }

    [HttpPost]
    public async Task<IActionResult> Add([FromBody] Recipe recipe)
    {
        try
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(new { error = "Invalid recipe data", details = ModelState });
            }

            // Reset ID to ensure it's auto-generated
            recipe.Id = 0;

            _context.Recipes.Add(recipe);
            await _context.SaveChangesAsync();

            return Ok(new { message = "Recipe added successfully", recipe = recipe });
        }
        catch (Exception ex)
        {
            return StatusCode(500, new { error = "Failed to add recipe", details = ex.Message });
        }
    }

    [HttpPut]
    public async Task<IActionResult> Update([FromBody] Recipe recipe)
    {
        try
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(new { error = "Invalid recipe data", details = ModelState });
            }

            if (recipe.Id <= 0)
            {
                return BadRequest(new { error = "Invalid recipe ID" });
            }

            var existingRecipe = await _context.Recipes.FindAsync(recipe.Id);
            if (existingRecipe == null)
            {
                return NotFound(new { error = "Recipe not found" });
            }

            // Update properties
            existingRecipe.Name = recipe.Name;
            existingRecipe.Author = recipe.Author;
            existingRecipe.Type = recipe.Type;
            existingRecipe.Content = recipe.Content;

            await _context.SaveChangesAsync();
            return Ok(new { message = "Recipe updated successfully", recipe = existingRecipe });
        }
        catch (Exception ex)
        {
            return StatusCode(500, new { error = "Failed to update recipe", details = ex.Message });
        }
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> Delete(int id)
    {
        try
        {
            if (id <= 0)
            {
                return BadRequest(new { error = "Invalid recipe ID" });
            }

            var recipe = await _context.Recipes.FindAsync(id);
            if (recipe == null)
            {
                return NotFound(new { error = "Recipe not found" });
            }

            _context.Recipes.Remove(recipe);
            await _context.SaveChangesAsync();

            return Ok(new { message = "Recipe deleted successfully" });
        }
        catch (Exception ex)
        {
            return StatusCode(500, new { error = "Failed to delete recipe", details = ex.Message });
        }
    }
}


