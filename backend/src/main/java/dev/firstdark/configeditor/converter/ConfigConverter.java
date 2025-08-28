package dev.firstdark.configeditor.converter;

import com.google.gson.*;
import lombok.Getter;
import me.hypherionmc.moonconfig.core.CommentedConfig;
import me.hypherionmc.moonconfig.core.Config;
import me.hypherionmc.moonconfig.core.io.ParsingMode;
import me.hypherionmc.moonconfig.toml.TomlParser;
import me.hypherionmc.moonconfig.toml.TomlWriter;

import java.io.InputStream;
import java.util.*;

public class ConfigConverter {

    public static final ConfigConverter INSTANCE = new ConfigConverter();

    @Getter
    private final Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();

    @Getter
    private final TomlParser tomlParser = new TomlParser();
    private final TomlWriter tomlWriter = new TomlWriter();

    protected ConfigConverter() {
        Config.setInsertionOrderPreserved(true);
    }

    public JsonObject readToJson(InputStream tomlContent) {
        CommentedConfig config = CommentedConfig.inMemory();
        HashMap<String, String> comments = new LinkedHashMap<>();
        JsonObject configJson = new JsonObject();

        tomlParser.parse(tomlContent, config, ParsingMode.ADD);
        tomlToJson(config, configJson, comments, "");

        JsonObject finalObject = new JsonObject();
        finalObject.add("config", configJson);
        finalObject.add("comments", gson.toJsonTree(comments));

        return finalObject;
    }

    public String writeToToml(JsonObject configJson) {
        CommentedConfig config = CommentedConfig.inMemory();
        jsonToToml(config, configJson.getAsJsonObject("config"), "");

        JsonObject comments = configJson.getAsJsonObject("comments");
        comments.entrySet().forEach(e -> {
            try {
                config.setComment(e.getKey(), e.getValue().getAsString());
            } catch (Exception ignored) {}
        });

        return tomlWriter.writeToString(config);
    }

    private void tomlToJson(CommentedConfig newConfig, JsonObject values, HashMap<String, String> comments, String subKey) {
        newConfig.valueMap().forEach((key, value) -> {
            String finalKey = subKey.isEmpty() ? key : subKey + "." + key;

            if (value instanceof CommentedConfig commentedConfig) {
                JsonObject subSectionJson = new JsonObject();
                values.add(key, subSectionJson);
                newConfig.getOptionalComment(key).ifPresent(comment -> comments.put(finalKey, comment));
                tomlToJson(commentedConfig, subSectionJson, comments, finalKey);
            } else {
                JsonObject targetObject = values.has(key) ? values.getAsJsonObject(key) : values;

                if (value instanceof String s) {
                    targetObject.addProperty(key, s);
                    newConfig.getOptionalComment(key).ifPresent(comment -> comments.put(finalKey, comment));
                } else if (value instanceof Number n) {
                    if (key.equalsIgnoreCase("applicationid")) {
                        targetObject.addProperty(key, n.toString());
                    } else {
                        targetObject.addProperty(key, n);
                    }
                    newConfig.getOptionalComment(key).ifPresent(comment -> comments.put(finalKey, comment));
                } else if (value instanceof Boolean b) {
                    targetObject.addProperty(key, b);
                    newConfig.getOptionalComment(key).ifPresent(comment -> comments.put(finalKey, comment));
                } else if (value instanceof Collection<?> collection)  {
                    if (collection.isEmpty()) {
                        targetObject.add(key, new JsonArray());
                        newConfig.getOptionalComment(key).ifPresent(comment -> comments.put(finalKey, comment));
                    } else {
                        JsonArray stringArray = new JsonArray();
                        for (Object item : collection) {
                            if (item instanceof String) {
                                stringArray.add(new JsonPrimitive((String) item));
                                newConfig.getOptionalComment(key).ifPresent(comment -> comments.put(finalKey, comment));
                            } else if (item instanceof CommentedConfig nestedConfig) {
                                JsonObject nestedJson = new JsonObject();
                                tomlToJson(nestedConfig, nestedJson, comments, finalKey);
                                stringArray.add(nestedJson);
                            } else {
                                stringArray.add(new JsonPrimitive(String.valueOf(item)));
                                newConfig.getOptionalComment(key).ifPresent(comment -> comments.put(finalKey, comment));
                                //throw new RuntimeException(finalKey + " has an invalid value");
                            }
                        }
                        targetObject.add(key, stringArray);
                    }
                } else {
                    throw new RuntimeException(key + " is not a valid type");
                }
            }
        });
    }

    private void jsonToToml(CommentedConfig newConfig, JsonObject values, String subKey) {
        values.entrySet().forEach(entry -> {
            String key = entry.getKey();
            String finalKey = subKey.isEmpty() ? key : subKey + "." + key;

            if(entry.getValue().isJsonObject()) {
                CommentedConfig subConfig = CommentedConfig.inMemory();
                newConfig.add(key, subConfig);
                jsonToToml(subConfig, values.getAsJsonObject(key), finalKey);
            } else {
                if (entry.getValue().isJsonPrimitive()) {
                    JsonPrimitive primitive = entry.getValue().getAsJsonPrimitive();
                    if(primitive.isBoolean()) {
                        newConfig.add(key, primitive.getAsBoolean());
                    } else if(primitive.isNumber()) {
                        newConfig.add(key, primitive.getAsNumber());
                    } else if(primitive.isString()) {
                        int configVersion = values.get("version") == null ? 0 : values.get("version").getAsInt();

                        if (key.equalsIgnoreCase("applicationid") && configVersion < 24) {
                            newConfig.add(key, Long.parseLong(primitive.getAsString()));
                        } else {
                            newConfig.add(key, primitive.getAsString());
                        }
                    } else {
                        throw new RuntimeException(key + " is not a valid type");
                    }
                } else if (entry.getValue().isJsonArray()) {
                    JsonArray array = entry.getValue().getAsJsonArray();
                    if (!array.isEmpty() && array.get(0).isJsonPrimitive() && array.get(0).getAsJsonPrimitive().isString()) {
                        List<String> collection = new ArrayList<>();
                        array.forEach(item -> {
                            if (item.isJsonPrimitive() && item.getAsJsonPrimitive().isString()) {
                                collection.add(item.getAsString());
                            } else {
                                throw new RuntimeException(item + " is not a valid type");
                            }
                        });
                        newConfig.add(key, collection);
                    } else {
                        List<Object> collection = new ArrayList<>();
                        array.forEach(item -> {
                            if (item.isJsonObject()) {
                                CommentedConfig subConfig = CommentedConfig.inMemory();
                                jsonToToml(subConfig, item.getAsJsonObject(), finalKey);
                                collection.add(subConfig);
                            } else if (item.isJsonPrimitive()) {
                                JsonPrimitive primitive = item.getAsJsonPrimitive();
                                if (primitive.isBoolean()) {
                                    collection.add(primitive.getAsBoolean());
                                } else if (primitive.isNumber()) {
                                    collection.add(primitive.getAsNumber());
                                } else if (primitive.isString()) {
                                    collection.add(primitive.getAsString());
                                } else {
                                    throw new RuntimeException(item + " is not a valid type");
                                }
                            } else {
                                throw new RuntimeException(item + " is not a valid type");
                            }
                        });
                        newConfig.add(key, collection);
                    }
                } else {
                    throw new RuntimeException(key + " is not a valid type");
                }
            }
        });
    }
}
