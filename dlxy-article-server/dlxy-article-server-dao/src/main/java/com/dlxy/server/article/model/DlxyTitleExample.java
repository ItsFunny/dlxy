package com.dlxy.server.article.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DlxyTitleExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public DlxyTitleExample() {
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

        public Criteria andTitleIdIsNull() {
            addCriterion("title_id is null");
            return (Criteria) this;
        }

        public Criteria andTitleIdIsNotNull() {
            addCriterion("title_id is not null");
            return (Criteria) this;
        }

        public Criteria andTitleIdEqualTo(Integer value) {
            addCriterion("title_id =", value, "titleId");
            return (Criteria) this;
        }

        public Criteria andTitleIdNotEqualTo(Integer value) {
            addCriterion("title_id <>", value, "titleId");
            return (Criteria) this;
        }

        public Criteria andTitleIdGreaterThan(Integer value) {
            addCriterion("title_id >", value, "titleId");
            return (Criteria) this;
        }

        public Criteria andTitleIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("title_id >=", value, "titleId");
            return (Criteria) this;
        }

        public Criteria andTitleIdLessThan(Integer value) {
            addCriterion("title_id <", value, "titleId");
            return (Criteria) this;
        }

        public Criteria andTitleIdLessThanOrEqualTo(Integer value) {
            addCriterion("title_id <=", value, "titleId");
            return (Criteria) this;
        }

        public Criteria andTitleIdIn(List<Integer> values) {
            addCriterion("title_id in", values, "titleId");
            return (Criteria) this;
        }

        public Criteria andTitleIdNotIn(List<Integer> values) {
            addCriterion("title_id not in", values, "titleId");
            return (Criteria) this;
        }

        public Criteria andTitleIdBetween(Integer value1, Integer value2) {
            addCriterion("title_id between", value1, value2, "titleId");
            return (Criteria) this;
        }

        public Criteria andTitleIdNotBetween(Integer value1, Integer value2) {
            addCriterion("title_id not between", value1, value2, "titleId");
            return (Criteria) this;
        }

        public Criteria andTitleNameIsNull() {
            addCriterion("title_name is null");
            return (Criteria) this;
        }

        public Criteria andTitleNameIsNotNull() {
            addCriterion("title_name is not null");
            return (Criteria) this;
        }

        public Criteria andTitleNameEqualTo(String value) {
            addCriterion("title_name =", value, "titleName");
            return (Criteria) this;
        }

        public Criteria andTitleNameNotEqualTo(String value) {
            addCriterion("title_name <>", value, "titleName");
            return (Criteria) this;
        }

        public Criteria andTitleNameGreaterThan(String value) {
            addCriterion("title_name >", value, "titleName");
            return (Criteria) this;
        }

        public Criteria andTitleNameGreaterThanOrEqualTo(String value) {
            addCriterion("title_name >=", value, "titleName");
            return (Criteria) this;
        }

        public Criteria andTitleNameLessThan(String value) {
            addCriterion("title_name <", value, "titleName");
            return (Criteria) this;
        }

        public Criteria andTitleNameLessThanOrEqualTo(String value) {
            addCriterion("title_name <=", value, "titleName");
            return (Criteria) this;
        }

        public Criteria andTitleNameLike(String value) {
            addCriterion("title_name like", value, "titleName");
            return (Criteria) this;
        }

        public Criteria andTitleNameNotLike(String value) {
            addCriterion("title_name not like", value, "titleName");
            return (Criteria) this;
        }

        public Criteria andTitleNameIn(List<String> values) {
            addCriterion("title_name in", values, "titleName");
            return (Criteria) this;
        }

        public Criteria andTitleNameNotIn(List<String> values) {
            addCriterion("title_name not in", values, "titleName");
            return (Criteria) this;
        }

        public Criteria andTitleNameBetween(String value1, String value2) {
            addCriterion("title_name between", value1, value2, "titleName");
            return (Criteria) this;
        }

        public Criteria andTitleNameNotBetween(String value1, String value2) {
            addCriterion("title_name not between", value1, value2, "titleName");
            return (Criteria) this;
        }

        public Criteria andTitleParentIdIsNull() {
            addCriterion("title_parent_id is null");
            return (Criteria) this;
        }

        public Criteria andTitleParentIdIsNotNull() {
            addCriterion("title_parent_id is not null");
            return (Criteria) this;
        }

        public Criteria andTitleParentIdEqualTo(Integer value) {
            addCriterion("title_parent_id =", value, "titleParentId");
            return (Criteria) this;
        }

        public Criteria andTitleParentIdNotEqualTo(Integer value) {
            addCriterion("title_parent_id <>", value, "titleParentId");
            return (Criteria) this;
        }

        public Criteria andTitleParentIdGreaterThan(Integer value) {
            addCriterion("title_parent_id >", value, "titleParentId");
            return (Criteria) this;
        }

        public Criteria andTitleParentIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("title_parent_id >=", value, "titleParentId");
            return (Criteria) this;
        }

        public Criteria andTitleParentIdLessThan(Integer value) {
            addCriterion("title_parent_id <", value, "titleParentId");
            return (Criteria) this;
        }

        public Criteria andTitleParentIdLessThanOrEqualTo(Integer value) {
            addCriterion("title_parent_id <=", value, "titleParentId");
            return (Criteria) this;
        }

        public Criteria andTitleParentIdIn(List<Integer> values) {
            addCriterion("title_parent_id in", values, "titleParentId");
            return (Criteria) this;
        }

        public Criteria andTitleParentIdNotIn(List<Integer> values) {
            addCriterion("title_parent_id not in", values, "titleParentId");
            return (Criteria) this;
        }

        public Criteria andTitleParentIdBetween(Integer value1, Integer value2) {
            addCriterion("title_parent_id between", value1, value2, "titleParentId");
            return (Criteria) this;
        }

        public Criteria andTitleParentIdNotBetween(Integer value1, Integer value2) {
            addCriterion("title_parent_id not between", value1, value2, "titleParentId");
            return (Criteria) this;
        }

        public Criteria andTitleTypeIsNull() {
            addCriterion("title_type is null");
            return (Criteria) this;
        }

        public Criteria andTitleTypeIsNotNull() {
            addCriterion("title_type is not null");
            return (Criteria) this;
        }

        public Criteria andTitleTypeEqualTo(Integer value) {
            addCriterion("title_type =", value, "titleType");
            return (Criteria) this;
        }

        public Criteria andTitleTypeNotEqualTo(Integer value) {
            addCriterion("title_type <>", value, "titleType");
            return (Criteria) this;
        }

        public Criteria andTitleTypeGreaterThan(Integer value) {
            addCriterion("title_type >", value, "titleType");
            return (Criteria) this;
        }

        public Criteria andTitleTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("title_type >=", value, "titleType");
            return (Criteria) this;
        }

        public Criteria andTitleTypeLessThan(Integer value) {
            addCriterion("title_type <", value, "titleType");
            return (Criteria) this;
        }

        public Criteria andTitleTypeLessThanOrEqualTo(Integer value) {
            addCriterion("title_type <=", value, "titleType");
            return (Criteria) this;
        }

        public Criteria andTitleTypeIn(List<Integer> values) {
            addCriterion("title_type in", values, "titleType");
            return (Criteria) this;
        }

        public Criteria andTitleTypeNotIn(List<Integer> values) {
            addCriterion("title_type not in", values, "titleType");
            return (Criteria) this;
        }

        public Criteria andTitleTypeBetween(Integer value1, Integer value2) {
            addCriterion("title_type between", value1, value2, "titleType");
            return (Criteria) this;
        }

        public Criteria andTitleTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("title_type not between", value1, value2, "titleType");
            return (Criteria) this;
        }

        public Criteria andTitleDisplaySeqIsNull() {
            addCriterion("title_display_seq is null");
            return (Criteria) this;
        }

        public Criteria andTitleDisplaySeqIsNotNull() {
            addCriterion("title_display_seq is not null");
            return (Criteria) this;
        }

        public Criteria andTitleDisplaySeqEqualTo(Integer value) {
            addCriterion("title_display_seq =", value, "titleDisplaySeq");
            return (Criteria) this;
        }

        public Criteria andTitleDisplaySeqNotEqualTo(Integer value) {
            addCriterion("title_display_seq <>", value, "titleDisplaySeq");
            return (Criteria) this;
        }

        public Criteria andTitleDisplaySeqGreaterThan(Integer value) {
            addCriterion("title_display_seq >", value, "titleDisplaySeq");
            return (Criteria) this;
        }

        public Criteria andTitleDisplaySeqGreaterThanOrEqualTo(Integer value) {
            addCriterion("title_display_seq >=", value, "titleDisplaySeq");
            return (Criteria) this;
        }

        public Criteria andTitleDisplaySeqLessThan(Integer value) {
            addCriterion("title_display_seq <", value, "titleDisplaySeq");
            return (Criteria) this;
        }

        public Criteria andTitleDisplaySeqLessThanOrEqualTo(Integer value) {
            addCriterion("title_display_seq <=", value, "titleDisplaySeq");
            return (Criteria) this;
        }

        public Criteria andTitleDisplaySeqIn(List<Integer> values) {
            addCriterion("title_display_seq in", values, "titleDisplaySeq");
            return (Criteria) this;
        }

        public Criteria andTitleDisplaySeqNotIn(List<Integer> values) {
            addCriterion("title_display_seq not in", values, "titleDisplaySeq");
            return (Criteria) this;
        }

        public Criteria andTitleDisplaySeqBetween(Integer value1, Integer value2) {
            addCriterion("title_display_seq between", value1, value2, "titleDisplaySeq");
            return (Criteria) this;
        }

        public Criteria andTitleDisplaySeqNotBetween(Integer value1, Integer value2) {
            addCriterion("title_display_seq not between", value1, value2, "titleDisplaySeq");
            return (Criteria) this;
        }

        public Criteria andTitleAbbNameIsNull() {
            addCriterion("title_abb_name is null");
            return (Criteria) this;
        }

        public Criteria andTitleAbbNameIsNotNull() {
            addCriterion("title_abb_name is not null");
            return (Criteria) this;
        }

        public Criteria andTitleAbbNameEqualTo(String value) {
            addCriterion("title_abb_name =", value, "titleAbbName");
            return (Criteria) this;
        }

        public Criteria andTitleAbbNameNotEqualTo(String value) {
            addCriterion("title_abb_name <>", value, "titleAbbName");
            return (Criteria) this;
        }

        public Criteria andTitleAbbNameGreaterThan(String value) {
            addCriterion("title_abb_name >", value, "titleAbbName");
            return (Criteria) this;
        }

        public Criteria andTitleAbbNameGreaterThanOrEqualTo(String value) {
            addCriterion("title_abb_name >=", value, "titleAbbName");
            return (Criteria) this;
        }

        public Criteria andTitleAbbNameLessThan(String value) {
            addCriterion("title_abb_name <", value, "titleAbbName");
            return (Criteria) this;
        }

        public Criteria andTitleAbbNameLessThanOrEqualTo(String value) {
            addCriterion("title_abb_name <=", value, "titleAbbName");
            return (Criteria) this;
        }

        public Criteria andTitleAbbNameLike(String value) {
            addCriterion("title_abb_name like", value, "titleAbbName");
            return (Criteria) this;
        }

        public Criteria andTitleAbbNameNotLike(String value) {
            addCriterion("title_abb_name not like", value, "titleAbbName");
            return (Criteria) this;
        }

        public Criteria andTitleAbbNameIn(List<String> values) {
            addCriterion("title_abb_name in", values, "titleAbbName");
            return (Criteria) this;
        }

        public Criteria andTitleAbbNameNotIn(List<String> values) {
            addCriterion("title_abb_name not in", values, "titleAbbName");
            return (Criteria) this;
        }

        public Criteria andTitleAbbNameBetween(String value1, String value2) {
            addCriterion("title_abb_name between", value1, value2, "titleAbbName");
            return (Criteria) this;
        }

        public Criteria andTitleAbbNameNotBetween(String value1, String value2) {
            addCriterion("title_abb_name not between", value1, value2, "titleAbbName");
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