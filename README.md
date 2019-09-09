# spring-data-jpa-tip
JPA,, Spring Data JPA tricks and tips

## Entity proxy와 관련하여
Entity를 조회한 결과를 정렬하여 내보낼 필요가 있을 때 크게 두 방법이 있다.  
1. Entity를 정렬해서 DTO로 변환
2. Entity를 DTO로 변환해서 정렬

1번 케이스의 경우 Entity 자체에 Comparator 인터페이스를 구현하게 하고 비교하는 코드를 작성 할 수 있는데  
이때 getter가 아닌 field에 직접 접근할 경우 null pointer exception이 발생할수 있다.  

Lazy loading인 Entity의 경우 Proxy이므로 모든 field가 null이기 때문이다.  
이를 방지하려면 getter를 사용하거나 2번 방법을 사용하면 된다.


Proxy이므로 Null pointer exception 발생  
```java
 @Override
    public int compareTo(Drug o) {
        int i = this.name.compareTo(o.name);
        if (i == 0) {
            return this.dose.compareTo(o.dose) == 0
                    ? this.unit.compareTo(o.unit)
                    : this.dose.compareTo(o.dose);
        }
        return i;
    }
    
```

```java
 @Override
    public int compareTo(Drug o) {
        int i = this.getName().compareTo(o.getName());
        if (i == 0) {
            return this.getDose().compareTo(o.getDose()) == 0
                    ? this.getUnit().compareTo(o.getUnit())
                    : this.getDose().compareTo(o.getDose());
        }
        return i;
    }

```



## SequenceGenerator의 strategy를 SEQUENCE로 지정할 때 주의 할 점
Oracle, MariaDB 등 Sequence를 지원하는 DB에 매핑 시 allocatioinSize가 맞지 않으면 EntityExistsException이 발생할 수 있다.  
MariaDB 의 경우 기본 시퀀스 생성 시 increment 사이즈가 10이다. 하지만 JPA의 SequenceGenerator의 default allocationSize는 50이다. 

```java
public @interface SequenceGenerator {

    /** 
     * (Required) A unique generator name that can be referenced 
     * by one or more classes to be the generator for primary key 
     * values.
     */
    String name();

    /**
     * (Optional) The name of the database sequence object from 
     * which to obtain primary key values.
     * <p> Defaults to a provider-chosen value.
     */
    String sequenceName() default "";

    /** (Optional) The catalog of the sequence generator. 
     *
     * @since Java Persistence 2.0
     */
    String catalog() default "";

    /** (Optional) The schema of the sequence generator. 
     *
     * @since Java Persistence 2.0
     */
    String schema() default "";

    /** 
     * (Optional) The value from which the sequence object 
     * is to start generating.
     */
    int initialValue() default 1;

    /**
     * (Optional) The amount to increment by when allocating 
     * sequence numbers from the sequence.
     */
    int allocationSize() default 50;
}

```
이 때 여러 Entity를 생성하련느 경우 EntityExistsException: a different object with the same identifier value를 마주할 수 있다.  
보통 해당 키워드로 검색하면 cascade 옵션에 MERGE를 부여하라는 답변이 많으나 이미 적용되어있는 상황이었다.  
Local은 h2로 돌리다보니 문제가 없었는데 MariaDB에 실제 매핑을 하며 데이터를 넣다보니 발생한 예외였다. 




