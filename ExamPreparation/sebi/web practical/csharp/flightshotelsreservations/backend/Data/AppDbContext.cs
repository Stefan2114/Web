using Microsoft.EntityFrameworkCore;

public class AppDbContext : DbContext
{
    public DbSet<Flight> Flights { get; set; }
    public DbSet<Hotel> Hotels { get; set; }
    public DbSet<Reservation> Reservations { get; set; }

    public AppDbContext(DbContextOptions<AppDbContext> options) : base(options) { }

    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        modelBuilder.Entity<Flight>()
            .HasKey(p => p.FlightID);

        modelBuilder.Entity<Hotel>()
            .HasKey(c => c.HotelID);

        modelBuilder.Entity<Reservation>()
            .HasKey(c => c.Id);
    }
}