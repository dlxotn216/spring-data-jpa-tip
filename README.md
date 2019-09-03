# spring-data-jpa-tip
JPA,, Spring Data JPA tricks and tips

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
