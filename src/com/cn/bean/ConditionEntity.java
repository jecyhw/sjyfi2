package com.cn.bean;

import com.cn.util.DateUtil;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jecyhw on 2014/10/27.
 */
public class ConditionEntity {
    String recorder;
    Date startTime;
    Date endTime;
    String address;
    Double left;
    Double top;
    Double right;
    Double bottom;
    String sql = null;
    List sqlValues = null;

    public String getSql() {
        /*
        select b.* from (select t_tracks.* from t_tracks, (SELECT DISTINCT t_tracks_points.trackid FROM t_tracks_points WHERE
(( longitude>='-100.198368' and longitude<'181.461679' )
and( latitude>='-1.209283' and latitude<'180.959893' )))a where t_tracks.trackid = a.trackid)b;

        * */
        if (sql == null) {
            get();
        }
        return sql;
    }

    public List getSqlValues() {
        if (sqlValues == null) {
            get();
        }
        return sqlValues;
    }

    private void get() {
        if (sqlValues == null) {
            sqlValues = new ArrayList();
        }
        StringBuilder sb = new StringBuilder();
        boolean flag = false;
        if (top != null || left != null || right != null || bottom != null) {
            sb.append("select b.* from (select t_tracks.* from t_tracks, (select distinct t_tracks_points.trackid from t_tracks_points where (");

            if (left != null) {
                sb.append(" longitude >= ? ");
                sqlValues.add(left);
                //sb.append(SqlAssist.addGreatEqual("longitude", left.toString()));
                flag = true;
            }

            if (right != null) {
                if (flag) {
                    sb.append("and");
                }
                sb.append(" longitude <= ? ");
                sqlValues.add(right);
                //sb.append(SqlAssist.addLessEqual("longitude", right.toString()));
                flag = true;
            }

            if (top != null) {
                if (flag) {
                    sb.append("and");
                }
                sb.append(" latitude >= ? ");
                sqlValues.add(top);
                //sb.append(SqlAssist.addGreatEqual("latitude", top.toString()));
                flag = true;
            }

            if (bottom != null) {
                if (flag) {
                    sb.append("and");
                }
                sb.append(" latitude <= ? ");
                sqlValues.add(bottom);
                //sb.append(SqlAssist.addLessEqual("latitude", bottom.toString()));
                flag = true;
            }

            sb.append("))a where t_tracks.trackid = a.trackid)b");

            if (recorder != null || startTime != null || endTime != null || address != null) {
                sb.append(" where ");
            }
            else {
                sql = sb.toString();
                return ;
            }
        } else {
            sb.append("select distinct * from t_tracks where ");
        }

        flag = false;
        if (recorder != null) {
            sb.append(" author like ? ");
            sqlValues.add("%" + recorder + "%");
            //sb.append(SqlAssist.addEqual("author", recorder));
            flag = true;
        }
        if (startTime != null || endTime != null) {
            if (flag)
                sb.append("and");
            flag = false;
            sb.append("(");
            if (startTime != null) {
                flag = true;
                sb.append(" starttime >= ? ");
                sqlValues.add(DateUtil.date2String(startTime));
                //sb.append(SqlAssist.addGreatEqual("starttime", DateUtil.date2String(startTime)));
            }
            if (endTime != null) {
                if (flag)
                    sb.append("and");
                sb.append(" endtime <= ? ");
                sqlValues.add(DateUtil.date2String(endTime));
                //sb.append(SqlAssist.addLessEqual("endtime", DateUtil.date2String(endTime)));
            }
            sb.append(")");
            flag = true;
        }
        if (address != null) {
            if (flag)
                sb.append("or");
            sb.append("(").append(" name like ? ");
            sqlValues.add("%" + address + "%");
            sb.append("or").append(" keysiteslist like ? ");
            sqlValues.add("%" + address + "%");
            sb.append("or").append(" annotation like ? ").append(")");
            sqlValues.add("%" + address + "%");
            //sb.append("(").append(SqlAssist.addEqual("name", address));
            //sb.append("or").append(SqlAssist.addEqual("keysiteslist", address));
            //sb.append("or").append(SqlAssist.addEqual("annotation", address)).append(")");
            flag = true;
        }
        sql = sb.toString();
    }

    public String getRecorder() {
        return recorder;
    }

    public void setRecorder(String recorder) {
        this.recorder = recorder;
    }

    public double getBottom() {
        return bottom;
    }

    public void setBottom(double bottom) {
        this.bottom = bottom;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLeft() {
        return left;
    }

    public void setLeft(double left) {
        this.left = left;
    }

    public double getTop() {
        return top;
    }

    public void setTop(double top) {
        this.top = top;
    }

    public double getRight() {
        return right;
    }

    public void setRight(double right) {
        this.right = right;
    }

    @Override
    public String toString() {
        return "ConditionEntity{" +
                "recorder='" + recorder + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", address='" + address + '\'' +
                ", left=" + left +
                ", top=" + top +
                ", right=" + right +
                ", bottom=" + bottom +
                '}';
    }
}
