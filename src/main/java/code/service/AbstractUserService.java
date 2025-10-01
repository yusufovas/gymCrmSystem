package code.service;

import code.dao.BaseDao;
import code.model.User;
import code.utils.PasswordGenerator;
import code.utils.UsernameGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public abstract class AbstractUserService<T> {

    protected abstract BaseDao<T, UUID> getDao();
    protected abstract void validate(T entity);
    protected abstract void cascadeDeleteIfNeeded(T entity);
    protected abstract User getUser(T entity);

    protected final Logger log = LogManager.getLogger(getClass());

    @Transactional
    public T create(T entity, Predicate<String> usernameExists) {
        validate(entity);

        User user = getUser(entity);
        String username = UsernameGenerator.generate(
                user.getFirstName(),
                user.getLastName(),
                usernameExists
        );
        user.setUsername(username);
        user.setPassword(PasswordGenerator.generate());
        user.setActive(true);

        log.info("Creating profile for {}", user.getUsername());
        return getDao().create(entity);
    }

    @Transactional
    public T update(UUID id, T updatedEntity, Predicate<String> usernameExists) {
        return getDao().findById(id)
                .map(existing -> {
                    validate(updatedEntity);

                    User existingUser = getUser(existing);
                    User updatedUser = getUser(updatedEntity);

                    existingUser.setFirstName(updatedUser.getFirstName());
                    existingUser.setLastName(updatedUser.getLastName());

                    String newUsername = UsernameGenerator.generate(
                            updatedUser.getFirstName(),
                            updatedUser.getLastName(),
                            usernameExists
                    );
                    existingUser.setUsername(newUsername);

                    log.info("Updating profile for {}", existingUser.getUsername());
                    return getDao().update(existing);
                })
                .orElseThrow(() -> new RuntimeException("User not found with id=" + id));
    }

    @Transactional
    public void deleteByUsername(String username) {
        T entity = getDao().findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        cascadeDeleteIfNeeded(entity);
        getDao().delete(getUser(entity).getUserId());

        log.info("Deleted user={}", username);
    }

    @Transactional(readOnly = true)
    public boolean authenticate(String username, String password) {
        return getDao().findByUsername(username)
                .map(e -> getUser(e).getPassword().equals(password))
                .orElse(false);
    }

    @Transactional
    public void changePassword(String username, String oldPassword, String newPassword) {
        T entity = getDao().findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        User user = getUser(entity);
        if (!user.getPassword().equals(oldPassword)) {
            throw new RuntimeException("Old password does not match for username=" + username);
        }

        user.setPassword(newPassword);
        getDao().update(entity);

        log.info("Password changed for user={}", username);
    }

    @Transactional
    public void toggleActive(String username, boolean activate) {
        T entity = getDao().findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        User user = getUser(entity);
        if (user.isActive() == activate) {
            throw new RuntimeException("User already in desired state: " + (activate ? "active" : "inactive"));
        }

        user.setActive(activate);
        getDao().update(entity);

        log.info("User={} is now {}", username, activate ? "active" : "inactive");
    }

    @Transactional(readOnly = true)
    public List<T> getAll() {
        return getDao().findAll();
    }
}