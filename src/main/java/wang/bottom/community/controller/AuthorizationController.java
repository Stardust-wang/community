package wang.bottom.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
import wang.bottom.community.entity.AccessTokenParam;
import wang.bottom.community.entity.GitHubUser;
import wang.bottom.community.entity.User;
import wang.bottom.community.mapper.UserMapper;
import wang.bottom.community.provider.GitHubProvider;


@Controller
public class AuthorizationController {
    @Autowired
    GitHubProvider gitHubProvider;
    @Autowired
    UserMapper userMapper;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletResponse response){
        AccessTokenParam accessTokenParam = new AccessTokenParam();
        accessTokenParam.setCode(code);
        accessTokenParam.setState(state);
        accessTokenParam.setClient_id("8dbf6f98389e8a399faa");
        accessTokenParam.setClient_secret("77e73934cc6c692df95c91d81f2b6f8c4933b5dc");
        String Token = gitHubProvider.getAccessToken(accessTokenParam);  // 获取到access_token
        String accessToken = Token.split("&")[0].split("=")[1];
        GitHubUser gitHubUser = gitHubProvider.getGitHubUser(accessToken);  // 使用access_token获取用户信息
//        System.out.println(gitHubUser.getName());
        if(gitHubUser != null) {
            String token = UUID.randomUUID().toString();
            User selectUser = userMapper.selectUserByAccountId(gitHubUser.getId());  // 根据accountId查询是否存在该用户
            if (selectUser != null) {
                selectUser.setToken(token);
                if (gitHubUser.getName() != null && selectUser.getName() != null && !gitHubUser.getName().equals(selectUser.getName())) {
                    selectUser.setName(gitHubUser.getName());
                }
                selectUser.setModifiedTime(System.currentTimeMillis());
                userMapper.updateUser(selectUser);
            } else {  // 如果不存在添加新的用户信息
                User user = new User();
                user.setName(gitHubUser.getName());
                user.setAccountId(gitHubUser.getId());
                user.setToken(token);
                user.setCreateTime(System.currentTimeMillis());
                user.setModifiedTime(user.getCreateTime());
                userMapper.addUser(user);
            }
            response.addCookie(new Cookie("token",token));
            System.out.println(gitHubUser.getName());
        }
        return "redirect:/";
    }
}
