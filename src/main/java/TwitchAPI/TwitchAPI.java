package TwitchAPI;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.ITwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;

public final class TwitchAPI extends JavaPlugin {


        private ITwitchClient client;
        FileConfiguration config;
       public FileConfiguration data;
        public File dataf;
        String clientid;

        String token;
    @Override
    public void onEnable() {
        this.saveConfig();
        config = getConfig();

        dataf = new File(getDataFolder(), "data.yml");
        data = YamlConfiguration.loadConfiguration(dataf);

        clientid = config.getString("client_id");
        token = config.getString("oauth_token");

        OAuth2Credential credential = StringUtils.isNotBlank(token) ? new OAuth2Credential("twitch", token) : null;

        client = TwitchClientBuilder.builder()
                .withClientId(clientid)
                .withClientSecret(config.getString("client_secret"))
                .withEnableChat(true)
                .withChatAccount(credential)
                .withEnableHelix(true).withEnableTMI(true)
                .withDefaultAuthToken(credential).withDefaultEventHandler(SimpleEventHandler.class)
                .build();

        List<String> channels = config.getStringList("channels");
        if (!channels.isEmpty()) {
            channels.forEach(name -> client.getChat().joinChannel(name));
            client.getClientHelper().enableStreamEventListener(channels);
            client.getClientHelper().enableFollowEventListener(channels);
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public ITwitchClient getClient(){
        return this.client;
    }

    public String getClientid(){
        return clientid;
    }

    public String getToken(){
        return token;
    }

    public void saveFile(FileConfiguration f, File file){
        try{
            f.save(file);
        } catch (IOException ignored){

        }

    }

}
