public class AuthenticationMiddleware
{
    private readonly RequestDelegate _next;

    public AuthenticationMiddleware(RequestDelegate next)
    {
        _next = next;
    }

    public async Task InvokeAsync(HttpContext context)
    {
        var path = context.Request.Path.Value?.ToLower();
        if (path == "/api/auth/login" || path == "/" || path?.StartsWith("/swagger") == true)
        {
            await _next(context);
            return;
        }

        if (path?.StartsWith("/api") == true && path != "/api/auth/login")
        {
            var user = context.Session.GetString("user");
            if (string.IsNullOrEmpty(user))
            {
                context.Response.StatusCode = 401;
                await context.Response.WriteAsync("{\"error\": \"Unauthorized. Please log in.\"}");
                return;
            }
        }

        await _next(context);
    }
}