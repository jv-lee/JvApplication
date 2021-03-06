package com.jv.daily.entity;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "TOP_STORIES_BEAN".
*/
public class TopStoriesBeanDao extends AbstractDao<TopStoriesBean, Long> {

    public static final String TABLENAME = "TOP_STORIES_BEAN";

    /**
     * Properties of entity TopStoriesBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, long.class, "id", true, "_id");
        public final static Property Image = new Property(1, String.class, "image", false, "IMAGE");
        public final static Property Type = new Property(2, int.class, "type", false, "TYPE");
        public final static Property Ga_prefix = new Property(3, String.class, "ga_prefix", false, "GA_PREFIX");
        public final static Property Title = new Property(4, String.class, "title", false, "TITLE");
        public final static Property Date = new Property(5, String.class, "date", false, "DATE");
    };


    public TopStoriesBeanDao(DaoConfig config) {
        super(config);
    }
    
    public TopStoriesBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"TOP_STORIES_BEAN\" (" + //
                "\"_id\" INTEGER PRIMARY KEY NOT NULL ," + // 0: id
                "\"IMAGE\" TEXT," + // 1: image
                "\"TYPE\" INTEGER NOT NULL ," + // 2: type
                "\"GA_PREFIX\" TEXT," + // 3: ga_prefix
                "\"TITLE\" TEXT," + // 4: title
                "\"DATE\" TEXT);"); // 5: date
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"TOP_STORIES_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, TopStoriesBean entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
 
        String image = entity.getImage();
        if (image != null) {
            stmt.bindString(2, image);
        }
        stmt.bindLong(3, entity.getType());
 
        String ga_prefix = entity.getGa_prefix();
        if (ga_prefix != null) {
            stmt.bindString(4, ga_prefix);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(5, title);
        }
 
        String date = entity.getDate();
        if (date != null) {
            stmt.bindString(6, date);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, TopStoriesBean entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
 
        String image = entity.getImage();
        if (image != null) {
            stmt.bindString(2, image);
        }
        stmt.bindLong(3, entity.getType());
 
        String ga_prefix = entity.getGa_prefix();
        if (ga_prefix != null) {
            stmt.bindString(4, ga_prefix);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(5, title);
        }
 
        String date = entity.getDate();
        if (date != null) {
            stmt.bindString(6, date);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.getLong(offset + 0);
    }    

    @Override
    public TopStoriesBean readEntity(Cursor cursor, int offset) {
        TopStoriesBean entity = new TopStoriesBean( //
            cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // image
            cursor.getInt(offset + 2), // type
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // ga_prefix
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // title
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5) // date
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, TopStoriesBean entity, int offset) {
        entity.setId(cursor.getLong(offset + 0));
        entity.setImage(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setType(cursor.getInt(offset + 2));
        entity.setGa_prefix(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setTitle(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setDate(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(TopStoriesBean entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(TopStoriesBean entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    protected boolean hasKey(TopStoriesBean entity) {
        return false;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
