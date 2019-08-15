package com.dycm.applib1.db.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.zhuorui.securities.applib1.socket.vo.kline.MinuteKlineData;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "MINUTE_KLINE_DATA".
*/
public class MinuteKlineDataDao extends AbstractDao<MinuteKlineData, Long> {

    public static final String TABLENAME = "MINUTE_KLINE_DATA";

    /**
     * Properties of entity MinuteKlineData.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Vol = new Property(1, double.class, "vol", false, "VOL");
        public final static Property Amount = new Property(2, double.class, "amount", false, "AMOUNT");
        public final static Property OpenPrice = new Property(3, double.class, "openPrice", false, "OPEN_PRICE");
        public final static Property AvgPrice = new Property(4, double.class, "avgPrice", false, "AVG_PRICE");
        public final static Property Price = new Property(5, double.class, "price", false, "PRICE");
        public final static Property High = new Property(6, double.class, "high", false, "HIGH");
        public final static Property Low = new Property(7, double.class, "low", false, "LOW");
        public final static Property DateTime = new Property(8, long.class, "dateTime", false, "DATE_TIME");
    }


    public MinuteKlineDataDao(DaoConfig config) {
        super(config);
    }
    
    public MinuteKlineDataDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"MINUTE_KLINE_DATA\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"VOL\" REAL NOT NULL ," + // 1: vol
                "\"AMOUNT\" REAL NOT NULL ," + // 2: amount
                "\"OPEN_PRICE\" REAL NOT NULL ," + // 3: openPrice
                "\"AVG_PRICE\" REAL NOT NULL ," + // 4: avgPrice
                "\"PRICE\" REAL NOT NULL ," + // 5: price
                "\"HIGH\" REAL NOT NULL ," + // 6: high
                "\"LOW\" REAL NOT NULL ," + // 7: low
                "\"DATE_TIME\" INTEGER NOT NULL );"); // 8: dateTime
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"MINUTE_KLINE_DATA\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, MinuteKlineData entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindDouble(2, entity.getVol());
        stmt.bindDouble(3, entity.getAmount());
        stmt.bindDouble(4, entity.getOpenPrice());
        stmt.bindDouble(5, entity.getAvgPrice());
        stmt.bindDouble(6, entity.getPrice());
        stmt.bindDouble(7, entity.getHigh());
        stmt.bindDouble(8, entity.getLow());
        stmt.bindLong(9, entity.getDateTime());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, MinuteKlineData entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindDouble(2, entity.getVol());
        stmt.bindDouble(3, entity.getAmount());
        stmt.bindDouble(4, entity.getOpenPrice());
        stmt.bindDouble(5, entity.getAvgPrice());
        stmt.bindDouble(6, entity.getPrice());
        stmt.bindDouble(7, entity.getHigh());
        stmt.bindDouble(8, entity.getLow());
        stmt.bindLong(9, entity.getDateTime());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public MinuteKlineData readEntity(Cursor cursor, int offset) {
        MinuteKlineData entity = new MinuteKlineData( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getDouble(offset + 1), // vol
            cursor.getDouble(offset + 2), // amount
            cursor.getDouble(offset + 3), // openPrice
            cursor.getDouble(offset + 4), // avgPrice
            cursor.getDouble(offset + 5), // price
            cursor.getDouble(offset + 6), // high
            cursor.getDouble(offset + 7), // low
            cursor.getLong(offset + 8) // dateTime
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, MinuteKlineData entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setVol(cursor.getDouble(offset + 1));
        entity.setAmount(cursor.getDouble(offset + 2));
        entity.setOpenPrice(cursor.getDouble(offset + 3));
        entity.setAvgPrice(cursor.getDouble(offset + 4));
        entity.setPrice(cursor.getDouble(offset + 5));
        entity.setHigh(cursor.getDouble(offset + 6));
        entity.setLow(cursor.getDouble(offset + 7));
        entity.setDateTime(cursor.getLong(offset + 8));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(MinuteKlineData entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(MinuteKlineData entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(MinuteKlineData entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
