package email.backend.DTO;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class JwtUtil {
   private static final String SECRET_KEY = "HaMaDa BeL GaNzAbIl BeL 2OrOnFeL";

   public static String getSecretKey() {
      return SECRET_KEY;
   }

   public static String generateToken(UserDTO userDto) {
      Map<String, Object> claims = new HashMap<>();
      claims.put("id", userDto.getId());
      claims.put("name", userDto.getName());
      claims.put("email", userDto.getEmailAddress());
      // claims.put("password", userDto.getPassword());

      System.out.println("user dto id : " + userDto.getId());
      System.out.println("User dto address : " + userDto.getEmailAddress());
      
      return Jwts.builder()
            .setClaims(claims)
            .setSubject(userDto.getEmailAddress())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))    // Token valid for 10 hours
            .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), SignatureAlgorithm.HS256)
            .compact();
   }

   public static Claims extractClaims(String token) {
      return Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
            .build()
            .parseClaimsJws(token)
            .getBody();
   }

   public static UserDTO getUserFromToken(String token) {
      System.out.println("entered get user form token in jwt file");
      token = token.replaceAll("Bearer ", "");
      System.out.println(token);
      Claims claims = extractClaims(token);
      System.out.println("entered get user form token in jwt file 2");

      UserDTO user = new UserDTO();
      user.setId(Long.parseLong(claims.get("id").toString()));
      user.setName(claims.get("name").toString());
      user.setEmailAddress(claims.get("email").toString());
      // user.setPassword(claims.get("password").toString());
      return user;
   }

}
