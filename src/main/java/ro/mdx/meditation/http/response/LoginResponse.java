package ro.mdx.meditation.http.response;

public record LoginResponse(String jwtToken, long expirationTime, String role) {
}
