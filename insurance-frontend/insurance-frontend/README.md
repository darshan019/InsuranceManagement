# Insurance Management - Frontend

A simple React front-end for the Insurance Management Spring Boot backend.
Login first asks you to select a role (Admin / Agent / Customer), then asks for
email and password.

## Prerequisites

- Node.js 18 or higher (check with `node -v`)
- The Spring Boot backend running on `http://localhost:9090`

## Setup

1. Install dependencies:
   ```
   npm install
   ```

2. Start the dev server:
   ```
   npm run dev
   ```

3. Open the browser at `http://localhost:5173`

## IMPORTANT: Backend CORS fix

The Spring Boot backend does **not** have CORS enabled. The browser will
block API calls from the React app unless you add CORS configuration.

Open `InsuranceManagement/src/main/java/com/internship/InsuranceManagement/security/SecurityConfig.java`
and add the following:

1. Add imports at the top:
   ```java
   import org.springframework.security.config.Customizer;
   import org.springframework.web.cors.CorsConfiguration;
   import org.springframework.web.cors.CorsConfigurationSource;
   import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
   import java.util.List;
   ```

2. Add this bean inside the `SecurityConfig` class:
   ```java
   @Bean
   public CorsConfigurationSource corsConfigurationSource() {
       CorsConfiguration cfg = new CorsConfiguration();
       cfg.setAllowedOrigins(List.of("http://localhost:5173", "http://localhost:3000"));
       cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
       cfg.setAllowedHeaders(List.of("*"));
       cfg.setAllowCredentials(true);
       UrlBasedCorsConfigurationSource src = new UrlBasedCorsConfigurationSource();
       src.registerCorsConfiguration("/**", cfg);
       return src;
   }
   ```

3. In `securityFilterChain`, add `.cors(Customizer.withDefaults())` to the
   `http` chain (right after `.csrf(csrf -> csrf.disable())`):
   ```java
   http
       .csrf(csrf -> csrf.disable())
       .cors(Customizer.withDefaults())
       .sessionManagement(...)
       ...
   ```

Restart the Spring Boot app after making these changes.

## Project Structure

```
src/
  api.js                         - axios instance with JWT interceptor
  App.jsx                        - main router
  App.css                        - all styles (plain CSS)
  main.jsx                       - React entry point
  context/
    AuthContext.jsx              - login / logout / user state
  components/
    Navbar.jsx                   - top nav bar with role-specific links
    ProtectedRoute.jsx           - route guard
  pages/
    Login.jsx                    - role selection + credentials
    Register.jsx                 - customer self-registration
    AdminDashboard.jsx           - stats cards for admin
    AgentDashboard.jsx           - stats for agent
    CustomerDashboard.jsx        - stats for customer
    Agents.jsx                   - list / add / delete agents (admin only)
    Customers.jsx                - list customers (admin all / agent own)
    Policies.jsx                 - list policies (role-based)
    Claims.jsx                   - claims with approve/delete/file actions
    PolicyTemplates.jsx          - browse/create templates
    Payments.jsx                 - payment history
    BrowseTemplates.jsx          - customer view to buy a policy
```

## How login works

1. User opens `/login`
2. User picks a role: Admin, Agent, or Customer
3. User enters email + password
4. Frontend calls the correct endpoint:
   - `POST /auth/admin/login` for ADMIN
   - `POST /auth/agent/login` for AGENT
   - `POST /auth/customer/login` for CUSTOMER
5. Backend returns `{ token, role, email }`
6. Frontend stores the token in localStorage
7. Frontend looks up the user's numeric ID (adminId / agentId / customerId)
   and stores it too (needed for role-specific API calls)
8. Frontend redirects to the role-specific dashboard

All subsequent requests send `Authorization: Bearer <token>` automatically.

## Default backend URL

The backend URL is hard-coded to `http://localhost:9090` in `src/api.js`.
Change it there if your backend runs elsewhere.

## Note on the CUSTOMER userId lookup

The `LoginResponse` DTO only returns `{ token, role, email }` - not the
numeric user ID. After a successful customer login, the frontend tries to
call `/api/customers` to find its own ID, but **customers don't have
permission to list all customers**, so this lookup fails silently.

If you want customer-specific features (My Policies, Buy Policy) to work
reliably, the cleanest fix is to add the `customerId` (and `agentId`,
`adminId`) to the `LoginResponse` DTO on the backend:

```java
public class LoginResponse {
    private String token;
    private String role;
    private String email;
    private int userId;   // <-- add this
}
```

Then update `AuthController.customerLogin()` to return
`new LoginResponse(token, "CUSTOMER", email, customer.getCustomerId())`
and do the same for admin + agent. The frontend already reads `userId`
from the response if present.
