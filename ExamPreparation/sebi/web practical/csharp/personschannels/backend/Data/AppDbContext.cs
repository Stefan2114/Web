using Microsoft.EntityFrameworkCore;

public class AppDbContext : DbContext
{
    public DbSet<Person> Persons { get; set; }
    public DbSet<Channel> Channels { get; set; }

    public AppDbContext(DbContextOptions<AppDbContext> options) : base(options) { }

    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        modelBuilder.Entity<Person>()
            .HasKey(p => p.Id);

        modelBuilder.Entity<Channel>()
            .HasKey(c => c.Id);

        modelBuilder.Entity<Channel>()
            .Property(c => c.Name)
            .IsRequired()
            .HasMaxLength(100);
    }
}