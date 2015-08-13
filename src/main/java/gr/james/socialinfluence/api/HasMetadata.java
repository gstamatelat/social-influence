package gr.james.socialinfluence.api;

public interface HasMetadata {
    String getMeta(String key);

    void setMeta(String key, String value);

    void clearMeta();
}
