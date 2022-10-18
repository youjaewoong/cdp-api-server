

### argument setup
```
-Dapp.home=D:\log\firstApiServer
-Dspring.profiles.active=local
```

### DDL
```
CREATE TABLE sample 
(
    empno    INT NOT NULL,
    ename    VARCHAR(10),
    job      VARCHAR(9),
    mgr      INT,
    hiredate DATE,
    sal      NUMERIC(7, 2),
    comm     NUMERIC(7, 2),
    deptno   INT,
    CONSTRAINT emp_pk PRIMARY KEY (empno)
)
```