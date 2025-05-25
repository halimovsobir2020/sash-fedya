package uz.pdp.productservice.util;

import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;

@Service
public class OptimisticLockRetrier {

    public <T> T retry(Callable<T> callable) {
        for (int i = 0; i < 3; i++) {
            try {
                return callable.call();
            } catch (ObjectOptimisticLockingFailureException e) {
                continue;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        throw new RuntimeException("too many requests");
    }

    public void retry(Runnable runnable) {
        for (int i = 0; i < 3; i++) {
            try {
                runnable.run();
                return;
            } catch (ObjectOptimisticLockingFailureException e) {
                continue;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}
