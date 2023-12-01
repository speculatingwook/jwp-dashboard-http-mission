package nextstep.jwp.db;

import nextstep.jwp.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserRepository {

    private static final Map<String, User> database = new HashMap<>();

    static {
        final User user = new User(1L, "a", "a", "hkkang@woowahan.com");
        database.put(user.getAccount(), user);
    }

    public static void save(User user) {
        database.put(user.getAccount(), user);
    }

    public static Optional<User> findByAccount(String account) {
        return Optional.ofNullable(database.getOrDefault(account, null));
    }

    private InMemoryUserRepository() {}
}
