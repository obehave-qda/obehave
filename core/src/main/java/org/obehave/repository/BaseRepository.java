package org.obehave.repository;

import org.obehave.model.BaseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Serves as a base class for all sql repository implementations
 */
public abstract class BaseRepository<T extends BaseEntity> {
    private static final Logger log = LoggerFactory.getLogger(BaseRepository.class);
	protected Connection con;

	public BaseRepository(Connection con) {
		this.con = con;
	}

    public List<Long> save(T... entities) {
        log.trace("Saving {} entities in repository", entities.length);
		List<Long> savedIds = new ArrayList<>();

		for (T entity : entities) {
			savedIds.add(save(entity));
		}

        return savedIds;
    }

    public long save(T entity) {
        log.debug("Saving entity {} in repository", entity);
        if (!entity.isDirty()) {
            log.trace("Nothing to do - it isn't dirty enough");
            return entity.getId();
        }

        if (entity.isNew()) {
            log.trace("Entity is new - inserting {}", entity);
            return insert(entity);
        } else {
            log.trace("Entity is existent, doing an update for {}", entity);
            return update(entity);
        }
    }

    protected abstract long update(T entity);

    protected abstract long insert(T entity);

    public abstract Optional<T> findById(Long id);

    public abstract List<T> findAll();

    public abstract long delete(Long id);

    public long delete(T entity) {
        log.debug("Deleting entity {} from repository", entity);
        return delete(entity.getId());
    }

    protected final java.sql.Date toDate(LocalDateTime ldt) {
        return new java.sql.Date(java.util.Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant()).getTime());
    }

	/**
	 * Creates a new instance of T mapped with values from the current position in the given ResultSet
	 * @param rs a ResultSet containing information to create a new T. The ResultSet's {@code next()} won't be called.
	 * @return a new instance of T
	 */
    protected abstract T mapRow(ResultSet rs) throws SQLException;

    protected List<T> processResultSet(ResultSet rs) throws SQLException {
        if (rs == null) {
            throw new IllegalArgumentException("ResultSet mustn't be null.");
        }

        List<T> tempList = new ArrayList<>();
        while (rs.next()) {
            T instance = mapRow(rs);
            tempList.add(instance);
        }
        return tempList;
    }

    protected Optional<T> processUniqueResultSet(ResultSet rs) throws SQLException {
        if (rs == null) {
            throw new IllegalArgumentException("ResultSet mustn't be null.");
        }

        T instance = null;
        if (rs.next()) {
            instance = mapRow(rs);
        }
        return Optional.ofNullable(instance);
    }

	/**
	 * Set a new connection to use.
	 * @param con a new connection instance
	 */
	public void setConnection(Connection con) {
		this.con = con;
	}
}