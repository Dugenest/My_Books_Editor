# My Books Editor - Backend

## Description
Backend de l'application "My Books Editor", une plateforme de gestion d'achat de livres développée avec Spring Boot 3.

## Prérequis
- Java 21
- Maven 3.8+
- MySQL 8.0+

## Technologies
- **Spring Boot 3.2.2**: Framework Java pour le développement d'applications
- **Spring Data JPA**: Couche d'accès aux données
- **Spring Security**: Sécurité et authentification
- **JWT (JSON Web Token)**: Authentification basée sur des tokens
- **Spring WebSocket**: Communication bidirectionnelle en temps réel
- **Hibernate**: ORM (Object-Relational Mapping)
- **MySQL**: Base de données relationnelle
- **Maven**: Gestion des dépendances et build
- **Lombok**: Réduction du code boilerplate
- **SpringDoc OpenAPI**: Documentation API (Swagger)

## Structure du projet
```
backend/
├── src/
│   ├── main/
│   │   ├── java/com/afci/
│   │   │   ├── config/                # Configuration Spring
│   │   │   ├── controller/            # Contrôleurs REST
|   |   |   ├── data/                  # Entités  
│   │   │   ├── dto/                   # Objets de transfert de données
│   │   │   ├── exception/             # Gestion des exceptions
│   │   │   ├── repository/            # Accès à la base de données
│   │   │   ├── security/              # Configuration de sécurité
│   │   │   ├── service/               # Logique métier
│   │   │   └── MyBooksEditorApplication.java # Point d'entrée
│   │   ├── resources/
│   │   │   ├── application.properties # Configuration principale
│   │   │   ├── static/                # Ressources statiques
│   │   │   └── templates/             # Templates Thymeleaf
│   ├── test/                          # Tests unitaires et d'intégration
├── uploads/                           # Dossier pour les fichiers téléchargés
└── pom.xml                            # Configuration Maven
```

## Installation

### Configuration de la base de données
1. Installer MySQL 8.0+
2. Créer une base de données (la base sera créée automatiquement si elle n'existe pas, grâce à `createDatabaseIfNotExist=true`)

### Configuration du projet
1. Cloner le dépôt:
```bash
git clone [URL_DU_DEPOT]
cd [NOM_DU_DEPOT]/backend
```

2. Configurer les propriétés de connexion à la base de données dans `src/main/resources/application.properties`:
```properties
# Par défaut
spring.datasource.url=jdbc:mysql://localhost:3306/My_Books_Editor?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=root
```

## Exécution

### Développement
```bash
mvn spring-boot:run
```
L'application sera disponible à l'adresse [http://localhost:8111](http://localhost:8111)

### Production
```bash
mvn clean package
java -jar target/My_Books_Editor-0.0.1-SNAPSHOT.jar
```

## Architecture

### Couches d'application
L'application suit une architecture en couches:

1. **Contrôleur**: Gestion des requêtes HTTP et des WebSockets
2. **Service**: Logique métier et validation
3. **Repository**: Accès aux données et persistance
4. **Modèle**: Entités JPA et objets de domaine

### Flux de données
```
Client → Controller → DTO → Service → Repository → Entity → Database
```

## API REST

### Endpoints principaux

#### Authentification
- `POST /api/auth/register` - Inscription d'un nouvel utilisateur
- `POST /api/auth/login` - Connexion et obtention d'un JWT
- `POST /api/auth/refresh` - Rafraîchissement du JWT

#### Utilisateurs
- `GET /api/users` - Liste des utilisateurs (admin)
- `GET /api/users/{id}` - Détails d'un utilisateur
- `PUT /api/users/{id}` - Mise à jour d'un profil utilisateur
- `DELETE /api/users/{id}` - Suppression d'un utilisateur

#### Livres
- `GET /api/books` - Liste des livres
- `GET /api/books/{id}` - Détails d'un livre
- `POST /api/books` - Création d'un livre
- `PUT /api/books/{id}` - Mise à jour d'un livre
- `DELETE /api/books/{id}` - Suppression d'un livre
- `GET /api/books/search` - Recherche de livres

#### Commandes
- `GET /api/orders` - Liste des commandes de l'utilisateur
- `GET /api/orders/{id}` - Détails d'une commande
- `POST /api/orders` - Création d'une commande
- `PUT /api/orders/{id}/status` - Mise à jour du statut d'une commande

### Documentation API
La documentation complète de l'API est disponible avec Swagger UI:
```
http://localhost:8111/swagger-ui/index.html
```

## Sécurité

### Authentification JWT
L'application utilise JWT (JSON Web Token) pour l'authentification:

1. L'utilisateur s'authentifie avec ses identifiants
2. Le serveur génère un JWT signé
3. Le client inclut ce token dans l'en-tête `Authorization` pour les requêtes suivantes
4. Le serveur valide le token pour chaque requête

### Configuration de sécurité
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors().and()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/auth/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
    
    // Autres beans et configurations...
}
```

## WebSockets

### Configuration WebSocket
```java
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins("http://localhost:8080")
                .withSockJS();
    }
}
```

### Utilisation des WebSockets
Les WebSockets sont utilisés pour:
- Notifications en temps réel
- Mise à jour des statuts de commande
- Communication entre utilisateurs/administrateurs

## Gestion des fichiers

### Upload de fichiers
L'application permet l'upload d'images (couvertures de livres, avatars utilisateurs):

```java
@RestController
@RequestMapping("/api/files")
public class FileController {
    
    @Value("${file.upload-dir}")
    private String uploadDir;
    
    @PostMapping("/upload")
    public ResponseEntity<FileUploadResponse> uploadFile(@RequestParam("file") MultipartFile file) {
        // Logique d'upload
    }
    
    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        // Logique pour servir le fichier
    }
}
```

## Validation des données

### Validation avec Bean Validation (JSR-380)
```java
public class BookDTO {
    
    @NotNull(message = "L'identifiant est obligatoire")
    private Long id;
    
    @NotBlank(message = "Le titre est obligatoire")
    @Size(max = 255, message = "Le titre ne doit pas dépasser 255 caractères")
    private String title;
    
    @NotBlank(message = "L'auteur est obligatoire")
    private String author;
    
    @NotNull(message = "Le prix est obligatoire")
    @Positive(message = "Le prix doit être positif")
    private BigDecimal price;
    
    // Autres champs et méthodes
}
```

## Tests

### Tests unitaires
Utilisation de JUnit 5, Mockito et AssertJ pour les tests unitaires:

```java
@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    
    @Mock
    private BookRepository bookRepository;
    
    @InjectMocks
    private BookServiceImpl bookService;
    
    @Test
    void shouldReturnBookWhenBookExists() {
        // Configuration du mock
        Book expectedBook = new Book();
        expectedBook.setId(1L);
        expectedBook.setTitle("Test Book");
        
        when(bookRepository.findById(1L)).thenReturn(Optional.of(expectedBook));
        
        // Appel de la méthode à tester
        Book actualBook = bookService.getBookById(1L);
        
        // Vérifications
        assertThat(actualBook).isNotNull();
        assertThat(actualBook.getTitle()).isEqualTo("Test Book");
        verify(bookRepository).findById(1L);
    }
}
```

### Tests d'intégration
Utilisation de `@SpringBootTest` pour les tests d'intégration:

```java
@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    void shouldReturnBookWhenBookExists() throws Exception {
        mockMvc.perform(get("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").isNotEmpty());
    }
}
```

## Variables d'environnement
L'application peut être configurée avec les variables d'environnement suivantes:

| Variable | Description | Valeur par défaut |
|----------|-------------|-------------------|
| `ADMIN_USERNAME` | Nom d'utilisateur admin | admin |
| `ADMIN_PASSWORD` | Mot de passe admin | secure_password |
| `JWT_SECRET` | Clé secrète pour les JWT | mySecretKey123456789012345678901234567890 |
| `FRONTEND_URL` | URL du frontend | http://localhost:8080 |
| `CORS_ALLOWED_ORIGINS` | Origines autorisées pour CORS | http://localhost:8080 |

## Déploiement

### Compilation
```bash
mvn clean package
```

### Exécution en production
```bash
java -jar target/My_Books_Editor-0.0.1-SNAPSHOT.jar \
  --spring.datasource.url=jdbc:mysql://db-host:3306/books_db \
  --spring.datasource.username=prod_user \
  --spring.datasource.password=prod_password \
  --jwt.secret=production_secret_key \
  --app.frontend-url=https://my-books-editor.com
```

### Docker (exemple)
```dockerfile
FROM openjdk:21-jdk-slim
VOLUME /tmp
COPY target/My_Books_Editor-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

## Maintenance et déboggage

### Logs
Les logs sont configurés avec différents niveaux selon les packages:
```properties
logging.level.org.hibernate=WARN
logging.level.org.springframework=WARN
logging.level.com.afci=INFO
logging.level.org.springframework.security=WARN
```

### Profils Spring Boot
L'application peut utiliser différents profils:
- `dev`: Pour le développement local
- `test`: Pour les tests
- `prod`: Pour la production

Exemple d'activation:
```bash
java -jar app.jar --spring.profiles.active=prod
```

## Ressources
- [Documentation Spring Boot](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Documentation Spring Data JPA](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Documentation Spring Security](https://docs.spring.io/spring-security/reference/)