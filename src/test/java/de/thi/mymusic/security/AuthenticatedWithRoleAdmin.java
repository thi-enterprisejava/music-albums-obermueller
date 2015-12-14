package de.thi.mymusic.security;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RunAs;
import javax.ejb.Stateless;
import java.util.concurrent.Callable;

/**
 * Created by Michael on 13.12.2015.
 */

@Stateless
@RunAs("Admin")
@PermitAll
public class AuthenticatedWithRoleAdmin {

    public void call(Callable callable) throws Exception {
        callable.call();
    }

    public void run(Runnable runable) throws Exception {
        runable.run();
    }
}
