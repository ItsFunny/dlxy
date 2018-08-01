package com.dlxy.server.user.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DlxyUserIllegalLogExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public DlxyUserIllegalLogExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIllegalLogIdIsNull() {
            addCriterion("illegal_log_id is null");
            return (Criteria) this;
        }

        public Criteria andIllegalLogIdIsNotNull() {
            addCriterion("illegal_log_id is not null");
            return (Criteria) this;
        }

        public Criteria andIllegalLogIdEqualTo(Long value) {
            addCriterion("illegal_log_id =", value, "illegalLogId");
            return (Criteria) this;
        }

        public Criteria andIllegalLogIdNotEqualTo(Long value) {
            addCriterion("illegal_log_id <>", value, "illegalLogId");
            return (Criteria) this;
        }

        public Criteria andIllegalLogIdGreaterThan(Long value) {
            addCriterion("illegal_log_id >", value, "illegalLogId");
            return (Criteria) this;
        }

        public Criteria andIllegalLogIdGreaterThanOrEqualTo(Long value) {
            addCriterion("illegal_log_id >=", value, "illegalLogId");
            return (Criteria) this;
        }

        public Criteria andIllegalLogIdLessThan(Long value) {
            addCriterion("illegal_log_id <", value, "illegalLogId");
            return (Criteria) this;
        }

        public Criteria andIllegalLogIdLessThanOrEqualTo(Long value) {
            addCriterion("illegal_log_id <=", value, "illegalLogId");
            return (Criteria) this;
        }

        public Criteria andIllegalLogIdIn(List<Long> values) {
            addCriterion("illegal_log_id in", values, "illegalLogId");
            return (Criteria) this;
        }

        public Criteria andIllegalLogIdNotIn(List<Long> values) {
            addCriterion("illegal_log_id not in", values, "illegalLogId");
            return (Criteria) this;
        }

        public Criteria andIllegalLogIdBetween(Long value1, Long value2) {
            addCriterion("illegal_log_id between", value1, value2, "illegalLogId");
            return (Criteria) this;
        }

        public Criteria andIllegalLogIdNotBetween(Long value1, Long value2) {
            addCriterion("illegal_log_id not between", value1, value2, "illegalLogId");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNull() {
            addCriterion("user_id is null");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNotNull() {
            addCriterion("user_id is not null");
            return (Criteria) this;
        }

        public Criteria andUserIdEqualTo(Long value) {
            addCriterion("user_id =", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotEqualTo(Long value) {
            addCriterion("user_id <>", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThan(Long value) {
            addCriterion("user_id >", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThanOrEqualTo(Long value) {
            addCriterion("user_id >=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThan(Long value) {
            addCriterion("user_id <", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThanOrEqualTo(Long value) {
            addCriterion("user_id <=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdIn(List<Long> values) {
            addCriterion("user_id in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotIn(List<Long> values) {
            addCriterion("user_id not in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdBetween(Long value1, Long value2) {
            addCriterion("user_id between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotBetween(Long value1, Long value2) {
            addCriterion("user_id not between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andIpIsNull() {
            addCriterion("ip is null");
            return (Criteria) this;
        }

        public Criteria andIpIsNotNull() {
            addCriterion("ip is not null");
            return (Criteria) this;
        }

        public Criteria andIpEqualTo(String value) {
            addCriterion("ip =", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpNotEqualTo(String value) {
            addCriterion("ip <>", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpGreaterThan(String value) {
            addCriterion("ip >", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpGreaterThanOrEqualTo(String value) {
            addCriterion("ip >=", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpLessThan(String value) {
            addCriterion("ip <", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpLessThanOrEqualTo(String value) {
            addCriterion("ip <=", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpLike(String value) {
            addCriterion("ip like", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpNotLike(String value) {
            addCriterion("ip not like", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpIn(List<String> values) {
            addCriterion("ip in", values, "ip");
            return (Criteria) this;
        }

        public Criteria andIpNotIn(List<String> values) {
            addCriterion("ip not in", values, "ip");
            return (Criteria) this;
        }

        public Criteria andIpBetween(String value1, String value2) {
            addCriterion("ip between", value1, value2, "ip");
            return (Criteria) this;
        }

        public Criteria andIpNotBetween(String value1, String value2) {
            addCriterion("ip not between", value1, value2, "ip");
            return (Criteria) this;
        }

        public Criteria andIllegalDetailIsNull() {
            addCriterion("illegal_detail is null");
            return (Criteria) this;
        }

        public Criteria andIllegalDetailIsNotNull() {
            addCriterion("illegal_detail is not null");
            return (Criteria) this;
        }

        public Criteria andIllegalDetailEqualTo(String value) {
            addCriterion("illegal_detail =", value, "illegalDetail");
            return (Criteria) this;
        }

        public Criteria andIllegalDetailNotEqualTo(String value) {
            addCriterion("illegal_detail <>", value, "illegalDetail");
            return (Criteria) this;
        }

        public Criteria andIllegalDetailGreaterThan(String value) {
            addCriterion("illegal_detail >", value, "illegalDetail");
            return (Criteria) this;
        }

        public Criteria andIllegalDetailGreaterThanOrEqualTo(String value) {
            addCriterion("illegal_detail >=", value, "illegalDetail");
            return (Criteria) this;
        }

        public Criteria andIllegalDetailLessThan(String value) {
            addCriterion("illegal_detail <", value, "illegalDetail");
            return (Criteria) this;
        }

        public Criteria andIllegalDetailLessThanOrEqualTo(String value) {
            addCriterion("illegal_detail <=", value, "illegalDetail");
            return (Criteria) this;
        }

        public Criteria andIllegalDetailLike(String value) {
            addCriterion("illegal_detail like", value, "illegalDetail");
            return (Criteria) this;
        }

        public Criteria andIllegalDetailNotLike(String value) {
            addCriterion("illegal_detail not like", value, "illegalDetail");
            return (Criteria) this;
        }

        public Criteria andIllegalDetailIn(List<String> values) {
            addCriterion("illegal_detail in", values, "illegalDetail");
            return (Criteria) this;
        }

        public Criteria andIllegalDetailNotIn(List<String> values) {
            addCriterion("illegal_detail not in", values, "illegalDetail");
            return (Criteria) this;
        }

        public Criteria andIllegalDetailBetween(String value1, String value2) {
            addCriterion("illegal_detail between", value1, value2, "illegalDetail");
            return (Criteria) this;
        }

        public Criteria andIllegalDetailNotBetween(String value1, String value2) {
            addCriterion("illegal_detail not between", value1, value2, "illegalDetail");
            return (Criteria) this;
        }

        public Criteria andIllegalLevelIsNull() {
            addCriterion("illegal_level is null");
            return (Criteria) this;
        }

        public Criteria andIllegalLevelIsNotNull() {
            addCriterion("illegal_level is not null");
            return (Criteria) this;
        }

        public Criteria andIllegalLevelEqualTo(Boolean value) {
            addCriterion("illegal_level =", value, "illegalLevel");
            return (Criteria) this;
        }

        public Criteria andIllegalLevelNotEqualTo(Boolean value) {
            addCriterion("illegal_level <>", value, "illegalLevel");
            return (Criteria) this;
        }

        public Criteria andIllegalLevelGreaterThan(Boolean value) {
            addCriterion("illegal_level >", value, "illegalLevel");
            return (Criteria) this;
        }

        public Criteria andIllegalLevelGreaterThanOrEqualTo(Boolean value) {
            addCriterion("illegal_level >=", value, "illegalLevel");
            return (Criteria) this;
        }

        public Criteria andIllegalLevelLessThan(Boolean value) {
            addCriterion("illegal_level <", value, "illegalLevel");
            return (Criteria) this;
        }

        public Criteria andIllegalLevelLessThanOrEqualTo(Boolean value) {
            addCriterion("illegal_level <=", value, "illegalLevel");
            return (Criteria) this;
        }

        public Criteria andIllegalLevelIn(List<Boolean> values) {
            addCriterion("illegal_level in", values, "illegalLevel");
            return (Criteria) this;
        }

        public Criteria andIllegalLevelNotIn(List<Boolean> values) {
            addCriterion("illegal_level not in", values, "illegalLevel");
            return (Criteria) this;
        }

        public Criteria andIllegalLevelBetween(Boolean value1, Boolean value2) {
            addCriterion("illegal_level between", value1, value2, "illegalLevel");
            return (Criteria) this;
        }

        public Criteria andIllegalLevelNotBetween(Boolean value1, Boolean value2) {
            addCriterion("illegal_level not between", value1, value2, "illegalLevel");
            return (Criteria) this;
        }

        public Criteria andCreateDateIsNull() {
            addCriterion("create_date is null");
            return (Criteria) this;
        }

        public Criteria andCreateDateIsNotNull() {
            addCriterion("create_date is not null");
            return (Criteria) this;
        }

        public Criteria andCreateDateEqualTo(Date value) {
            addCriterion("create_date =", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateNotEqualTo(Date value) {
            addCriterion("create_date <>", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateGreaterThan(Date value) {
            addCriterion("create_date >", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateGreaterThanOrEqualTo(Date value) {
            addCriterion("create_date >=", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateLessThan(Date value) {
            addCriterion("create_date <", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateLessThanOrEqualTo(Date value) {
            addCriterion("create_date <=", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateIn(List<Date> values) {
            addCriterion("create_date in", values, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateNotIn(List<Date> values) {
            addCriterion("create_date not in", values, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateBetween(Date value1, Date value2) {
            addCriterion("create_date between", value1, value2, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateNotBetween(Date value1, Date value2) {
            addCriterion("create_date not between", value1, value2, "createDate");
            return (Criteria) this;
        }

        public Criteria andUpdateDateIsNull() {
            addCriterion("update_date is null");
            return (Criteria) this;
        }

        public Criteria andUpdateDateIsNotNull() {
            addCriterion("update_date is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateDateEqualTo(Date value) {
            addCriterion("update_date =", value, "updateDate");
            return (Criteria) this;
        }

        public Criteria andUpdateDateNotEqualTo(Date value) {
            addCriterion("update_date <>", value, "updateDate");
            return (Criteria) this;
        }

        public Criteria andUpdateDateGreaterThan(Date value) {
            addCriterion("update_date >", value, "updateDate");
            return (Criteria) this;
        }

        public Criteria andUpdateDateGreaterThanOrEqualTo(Date value) {
            addCriterion("update_date >=", value, "updateDate");
            return (Criteria) this;
        }

        public Criteria andUpdateDateLessThan(Date value) {
            addCriterion("update_date <", value, "updateDate");
            return (Criteria) this;
        }

        public Criteria andUpdateDateLessThanOrEqualTo(Date value) {
            addCriterion("update_date <=", value, "updateDate");
            return (Criteria) this;
        }

        public Criteria andUpdateDateIn(List<Date> values) {
            addCriterion("update_date in", values, "updateDate");
            return (Criteria) this;
        }

        public Criteria andUpdateDateNotIn(List<Date> values) {
            addCriterion("update_date not in", values, "updateDate");
            return (Criteria) this;
        }

        public Criteria andUpdateDateBetween(Date value1, Date value2) {
            addCriterion("update_date between", value1, value2, "updateDate");
            return (Criteria) this;
        }

        public Criteria andUpdateDateNotBetween(Date value1, Date value2) {
            addCriterion("update_date not between", value1, value2, "updateDate");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}