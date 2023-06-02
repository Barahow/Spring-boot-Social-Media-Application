package dev.social.media.security.config;

public class EnvironmentKey {
    private final  String secretKey;
    public EnvironmentKey() {
        // Retrieve the secret key from an environment variable
        this.secretKey = System.getenv("MY_APP_SECRET_KEY");

    }


    public String getSecretKey() {
        return secretKey;
    }
}
