package TwitchManager;

import TwitchAPI.TwitchAPI;
import com.github.philippheuer.events4j.core.EventManager;
import com.github.philippheuer.events4j.simple.domain.EventSubscriber;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.common.events.domain.EventUser;
import com.github.twitch4j.helix.domain.*;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.Collections;

public class TwitchManager {

    private TwitchAPI main = null;

    public TwitchManager(TwitchAPI main){
        this.main = main;
    }


    public int getSubs(User user){

        SubscriptionList result = main.getClient().getHelix().getSubscriptions(main.getToken(), main.getClientid(), null,null,null).execute();
        return result.getPoints();
    }


    public void onChat(){
        this.main.getClient().getEventManager().onEvent(ChannelMessageEvent.class, e->{
            EventUser user = e.getUser();
            String[] message = e.getMessage().split(" ");

            SubscriptionList result = main.getClient().getHelix().getSubscriptions(main.getToken(), main.getClientid(), null,null,null).execute();
           if(message[0].equalsIgnoreCase("!javasub")){

               if(StringUtils.isBlank(message[1])){
                   return;
               }

                result.getSubscriptions().forEach(s ->{
                    if(e.getUser().getId().equals(s.getUserId())){
                        main.data.set("Users", s.getUserName() + "." + message[1]);
                        main.data.set("Users", s.getUserName() + "." + "SUB");
                        main.saveFile(main.data, main.dataf);
                    }
                });
            }
           if(message[0].equalsIgnoreCase("!bedrocksub")){
               if(StringUtils.isBlank(message[1])){
                   return;
               }

               result.getSubscriptions().forEach(s ->{
                   if(e.getUser().getId().equals(s.getUserId())){
                       main.data.set("Users", s.getUserName() + "." + "*"+message[1]);
                       main.data.set("Users", s.getUserName() + "." + "SUB");
                       main.saveFile(main.data, main.dataf);
                   }
               });
           }


        });





    }
}
