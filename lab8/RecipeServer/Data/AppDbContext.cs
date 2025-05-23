using Microsoft.EntityFrameworkCore;

public class AppDbContext : DbContext
{
    public AppDbContext(DbContextOptions<AppDbContext> options) : base(options) { }

    public DbSet<User> Users { get; set; }
    public DbSet<Recipe> Recipes { get; set; }

    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        modelBuilder.Entity<User>().HasData(
            new User { Id = 1, Username = "admin", Password = "admin123" },
            new User { Id = 2, Username = "user", Password = "user123" }
        );

        modelBuilder.Entity<Recipe>().HasData(
            new Recipe
            {
                Id = 1,
                Name = "Chocolate Cake",
                Author = "Chef John",
                Type = "Dessert",
                Content = "Mix flour, sugar, cocoa powder. Add eggs and milk. Bake at 350°F for 30 minutes."
            },
            new Recipe
            {
                Id = 2,
                Name = "Caesar Salad",
                Author = "Maria Garcia",
                Type = "Salad",
                Content = "Chop romaine lettuce. Add croutons, parmesan cheese, and caesar dressing. Toss well."
            }
        );
    }
}
