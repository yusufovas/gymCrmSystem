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
        log.debug("Entering create() with entity={}", entity);

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

        T created = getDao().create(entity);

        log.debug("Created entity with ID={}", getUser(created).getUserId());

        return created;
    }

    @Transactional
    public T update(UUID id, T updatedEntity, Predicate<String> usernameExists) {
        log.debug("Updating entity with ID={}", id);
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
        log.debug("Deleting user with username={}", username);
        T entity = getDao().findByUsername(username)
                .orElseThrow(() -> {
                    log.error("User not found: {}", username);
                    return new RuntimeException("User not found: " + username);
                });
        cascadeDeleteIfNeeded(entity);
        getDao().delete(getUser(entity).getUserId());

        log.info("Deleted user={}", username);
    }

    @Transactional(readOnly = true)
    public boolean authenticate(String username, String password) {
        log.debug("Authenticating user={}", username);

        boolean result = getDao().findByUsername(username)
                .map(e -> getUser(e).getPassword().equals(password))
                .orElse(false);

        log.info("Authentication {} for user={}", result ? "successful" : "failed", username);
        return result;
    }

    @Transactional
    public void changePassword(String username, String oldPassword, String newPassword) {
        log.debug("Changing password for user={}", username);

        T entity = getDao().findByUsername(username)
                .orElseThrow(() -> {
                    log.error("User not found: {}", username);
                    return new RuntimeException("User not found: " + username);
                });

        User user = getUser(entity);
        if (!user.getPassword().equals(oldPassword)) {
            log.error("Old password mismatch for username={}", username);
            throw new RuntimeException("Old password does not match for username=" + username);
        }

        user.setPassword(newPassword);
        getDao().update(entity);

        log.info("Password changed for user={}", username);
    }

    @Transactional
    public void toggleActive(String username, boolean activate) {
        log.debug("Toggling active state for user={} to {}", username, activate);

        T entity = getDao().findByUsername(username)
                .orElseThrow(() -> {
                    log.error("User not found: {}", username);
                    return new RuntimeException("User not found: " + username);
                });

        User user = getUser(entity);
        if (user.isActive() == activate) {
            log.warn("User={} already in desired state {}", username, activate);
            throw new RuntimeException("User already in desired state: " + (activate ? "active" : "inactive"));
        }

        user.setActive(activate);
        getDao().update(entity);

        log.info("User={} is now {}", username, activate ? "active" : "inactive");
    }

    public List<T> getAll() {
        log.debug("Fetching all entities of type={}", getClass().getSimpleName());
        return getDao().findAll();
    }
}