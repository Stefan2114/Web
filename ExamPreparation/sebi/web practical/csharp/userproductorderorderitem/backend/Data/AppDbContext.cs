using Microsoft.EntityFrameworkCore;

public class AppDbContext : DbContext
{
    public DbSet<User> Users { get; set; }
    public DbSet<Product> Products { get; set; }
    public DbSet<Order> Orders { get; set; }
    public DbSet<OrderItem> OrderItems { get; set; }

    public AppDbContext(DbContextOptions<AppDbContext> options) : base(options) { }

    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        modelBuilder.Entity<User>()
            .HasKey(p => p.Id);

        modelBuilder.Entity<Product>()
            .HasKey(c => c.Id);

        modelBuilder.Entity<Order>()
            .HasKey(c => c.Id);

        modelBuilder.Entity<OrderItem>()
            .HasKey(c => c.Id);
    }
}