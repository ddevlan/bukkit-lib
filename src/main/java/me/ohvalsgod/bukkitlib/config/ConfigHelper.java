package me.ohvalsgod.bukkitlib.config;

import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ConfigHelper {

    private File file;
    private YamlConfiguration configuration;
    @Setter private String root;

    public ConfigHelper(String name, File parentFolder) {
        this.file = new File(parentFolder + "/" + name + ".yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.root = "";
        this.configuration = YamlConfiguration.loadConfiguration(file);
    }

    public String getString(String path) {
        return translate(configuration.getString(this.root.isEmpty() ? path:this.root + "." + path));
    }

    public List<String> getStringList(String path) {
        return translate(configuration.getStringList(this.root.isEmpty() ? path:this.root + "." + path));
    }

    public Double getDouble(String path) {
        return configuration.getDouble(this.root.isEmpty() ? path:this.root + "." + path);
    }

    public List<Double> getDoubleList(String path) {
        return configuration.getDoubleList(this.root.isEmpty() ? path:this.root + "." + path);
    }

    public Boolean getBoolean(String path) {
        //TODO: format booleans for yes/no y/n t/f etc.
        return configuration.getBoolean(this.root.isEmpty() ? path:this.root + "." + path);
    }

    public Integer getInteger(String path) {
        return configuration.getInt(this.root.isEmpty() ? path:this.root + "." + path);
    }

    public List<Integer> getIntegerList(String path) {
        return configuration.getIntegerList(this.root.isEmpty() ? path:this.root + "." + path);
    }

    public ConfigHelper setRoot(String root) {
        this.root = root;
        return this;
    }

    private String translate(String source) {
        return ChatColor.translateAlternateColorCodes('&', source);
    }

    private List<String> translate(List<String> source) {
        source.forEach(this::translate);
        return source;
    }

    public void reload() {

    }

}
