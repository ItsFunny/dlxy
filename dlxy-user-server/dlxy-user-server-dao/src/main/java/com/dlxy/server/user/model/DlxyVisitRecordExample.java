package com.dlxy.server.user.model;

import java.util.ArrayList;
import java.util.List;

public class DlxyVisitRecordExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public DlxyVisitRecordExample() {
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

        public Criteria andVisitIdIsNull() {
            addCriterion("visit_id is null");
            return (Criteria) this;
        }

        public Criteria andVisitIdIsNotNull() {
            addCriterion("visit_id is not null");
            return (Criteria) this;
        }

        public Criteria andVisitIdEqualTo(Long value) {
            addCriterion("visit_id =", value, "visitId");
            return (Criteria) this;
        }

        public Criteria andVisitIdNotEqualTo(Long value) {
            addCriterion("visit_id <>", value, "visitId");
            return (Criteria) this;
        }

        public Criteria andVisitIdGreaterThan(Long value) {
            addCriterion("visit_id >", value, "visitId");
            return (Criteria) this;
        }

        public Criteria andVisitIdGreaterThanOrEqualTo(Long value) {
            addCriterion("visit_id >=", value, "visitId");
            return (Criteria) this;
        }

        public Criteria andVisitIdLessThan(Long value) {
            addCriterion("visit_id <", value, "visitId");
            return (Criteria) this;
        }

        public Criteria andVisitIdLessThanOrEqualTo(Long value) {
            addCriterion("visit_id <=", value, "visitId");
            return (Criteria) this;
        }

        public Criteria andVisitIdIn(List<Long> values) {
            addCriterion("visit_id in", values, "visitId");
            return (Criteria) this;
        }

        public Criteria andVisitIdNotIn(List<Long> values) {
            addCriterion("visit_id not in", values, "visitId");
            return (Criteria) this;
        }

        public Criteria andVisitIdBetween(Long value1, Long value2) {
            addCriterion("visit_id between", value1, value2, "visitId");
            return (Criteria) this;
        }

        public Criteria andVisitIdNotBetween(Long value1, Long value2) {
            addCriterion("visit_id not between", value1, value2, "visitId");
            return (Criteria) this;
        }

        public Criteria andVisitRecordTypeIsNull() {
            addCriterion("visit_record_type is null");
            return (Criteria) this;
        }

        public Criteria andVisitRecordTypeIsNotNull() {
            addCriterion("visit_record_type is not null");
            return (Criteria) this;
        }

        public Criteria andVisitRecordTypeEqualTo(Integer value) {
            addCriterion("visit_record_type =", value, "visitRecordType");
            return (Criteria) this;
        }

        public Criteria andVisitRecordTypeNotEqualTo(Integer value) {
            addCriterion("visit_record_type <>", value, "visitRecordType");
            return (Criteria) this;
        }

        public Criteria andVisitRecordTypeGreaterThan(Integer value) {
            addCriterion("visit_record_type >", value, "visitRecordType");
            return (Criteria) this;
        }

        public Criteria andVisitRecordTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("visit_record_type >=", value, "visitRecordType");
            return (Criteria) this;
        }

        public Criteria andVisitRecordTypeLessThan(Integer value) {
            addCriterion("visit_record_type <", value, "visitRecordType");
            return (Criteria) this;
        }

        public Criteria andVisitRecordTypeLessThanOrEqualTo(Integer value) {
            addCriterion("visit_record_type <=", value, "visitRecordType");
            return (Criteria) this;
        }

        public Criteria andVisitRecordTypeIn(List<Integer> values) {
            addCriterion("visit_record_type in", values, "visitRecordType");
            return (Criteria) this;
        }

        public Criteria andVisitRecordTypeNotIn(List<Integer> values) {
            addCriterion("visit_record_type not in", values, "visitRecordType");
            return (Criteria) this;
        }

        public Criteria andVisitRecordTypeBetween(Integer value1, Integer value2) {
            addCriterion("visit_record_type between", value1, value2, "visitRecordType");
            return (Criteria) this;
        }

        public Criteria andVisitRecordTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("visit_record_type not between", value1, value2, "visitRecordType");
            return (Criteria) this;
        }

        public Criteria andVisitCountIsNull() {
            addCriterion("visit_count is null");
            return (Criteria) this;
        }

        public Criteria andVisitCountIsNotNull() {
            addCriterion("visit_count is not null");
            return (Criteria) this;
        }

        public Criteria andVisitCountEqualTo(Integer value) {
            addCriterion("visit_count =", value, "visitCount");
            return (Criteria) this;
        }

        public Criteria andVisitCountNotEqualTo(Integer value) {
            addCriterion("visit_count <>", value, "visitCount");
            return (Criteria) this;
        }

        public Criteria andVisitCountGreaterThan(Integer value) {
            addCriterion("visit_count >", value, "visitCount");
            return (Criteria) this;
        }

        public Criteria andVisitCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("visit_count >=", value, "visitCount");
            return (Criteria) this;
        }

        public Criteria andVisitCountLessThan(Integer value) {
            addCriterion("visit_count <", value, "visitCount");
            return (Criteria) this;
        }

        public Criteria andVisitCountLessThanOrEqualTo(Integer value) {
            addCriterion("visit_count <=", value, "visitCount");
            return (Criteria) this;
        }

        public Criteria andVisitCountIn(List<Integer> values) {
            addCriterion("visit_count in", values, "visitCount");
            return (Criteria) this;
        }

        public Criteria andVisitCountNotIn(List<Integer> values) {
            addCriterion("visit_count not in", values, "visitCount");
            return (Criteria) this;
        }

        public Criteria andVisitCountBetween(Integer value1, Integer value2) {
            addCriterion("visit_count between", value1, value2, "visitCount");
            return (Criteria) this;
        }

        public Criteria andVisitCountNotBetween(Integer value1, Integer value2) {
            addCriterion("visit_count not between", value1, value2, "visitCount");
            return (Criteria) this;
        }

        public Criteria andVisitRecordDateIsNull() {
            addCriterion("visit_record_date is null");
            return (Criteria) this;
        }

        public Criteria andVisitRecordDateIsNotNull() {
            addCriterion("visit_record_date is not null");
            return (Criteria) this;
        }

        public Criteria andVisitRecordDateEqualTo(Long value) {
            addCriterion("visit_record_date =", value, "visitRecordDate");
            return (Criteria) this;
        }

        public Criteria andVisitRecordDateNotEqualTo(Long value) {
            addCriterion("visit_record_date <>", value, "visitRecordDate");
            return (Criteria) this;
        }

        public Criteria andVisitRecordDateGreaterThan(Long value) {
            addCriterion("visit_record_date >", value, "visitRecordDate");
            return (Criteria) this;
        }

        public Criteria andVisitRecordDateGreaterThanOrEqualTo(Long value) {
            addCriterion("visit_record_date >=", value, "visitRecordDate");
            return (Criteria) this;
        }

        public Criteria andVisitRecordDateLessThan(Long value) {
            addCriterion("visit_record_date <", value, "visitRecordDate");
            return (Criteria) this;
        }

        public Criteria andVisitRecordDateLessThanOrEqualTo(Long value) {
            addCriterion("visit_record_date <=", value, "visitRecordDate");
            return (Criteria) this;
        }

        public Criteria andVisitRecordDateIn(List<Long> values) {
            addCriterion("visit_record_date in", values, "visitRecordDate");
            return (Criteria) this;
        }

        public Criteria andVisitRecordDateNotIn(List<Long> values) {
            addCriterion("visit_record_date not in", values, "visitRecordDate");
            return (Criteria) this;
        }

        public Criteria andVisitRecordDateBetween(Long value1, Long value2) {
            addCriterion("visit_record_date between", value1, value2, "visitRecordDate");
            return (Criteria) this;
        }

        public Criteria andVisitRecordDateNotBetween(Long value1, Long value2) {
            addCriterion("visit_record_date not between", value1, value2, "visitRecordDate");
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