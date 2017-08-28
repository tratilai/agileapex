package ui_test;

import org.junit.Test;

import com.agileapex.domain.User;
import com.agileapex.domain.UserStatus;

public class LoginPageTest extends UITest {

    @Test
    public void test() throws Exception {
        User user = new User(1, "admin", null, null, "Admin", "Admin", UserStatus.REGISTERED, null, "s1", "123");
        openAndLogin(user, "admin");
        logout();
    }
}
