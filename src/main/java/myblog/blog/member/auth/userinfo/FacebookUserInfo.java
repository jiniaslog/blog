package myblog.blog.member.auth.userinfo;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Map;


public class FacebookUserInfo implements Oauth2UserInfo {

    private final Map<String, Object> attributes;

    public FacebookUserInfo(OAuth2User oAuth2User) {
        this.attributes = oAuth2User.getAttributes();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getProvider() {
        return ProviderType.FACEBOOK.getValue();
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getUserName() {
        return (String) attributes.get("name") +"#"+ ((String) attributes.get("id")).substring(0,5);
    }

    @Override
    public String getPicture() {
        return (String) attributes.get("picture");
    }
}