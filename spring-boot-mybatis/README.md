# Spring Boot 系列之MyBatis 

## MyBatis 简介
1. 基本概念	
  MyBatis is a first class persistence framework with support for custom SQL, stored procedures and advanced mappings. MyBatis eliminates almost all of the JDBC code and manual setting of parameters and retrieval of results. MyBatis can use simple XML or Annotations for configuration and map primitives, Map interfaces and Java POJOs (Plain Old Java Objects) to database records.

2. 历史
    `iBatis` 是 `MyBatis`的前生

## MyBatis 配置
### 全局XML配置文件
`MyBatis` 全局XML配置文件包含影响`MyBatis`行为的设置和属性。

 1. 属性（`properties`）

     配置属性时作为占位符使用

     ```xml
     <properties resource="org/mybatis/example/config.properties">
       <property name="username" value="dev_user"/>
       <property name="password" value="F2Fa3!33TYyg"/>
     </properties>
     
     <dataSource type="POOLED">
       <property name="driver" value="${driver}"/>
       <property name="url" value="${url}"/>
       <property name="username" value="${username}"/>
       <property name="password" value="${password}"/>
     </dataSource>
     ```

 2. 设置（`settings`）

     用于修改`MyBatis`运行时的行为

     ```xml
     <settings>
       <setting name="cacheEnabled" value="true"/>
       <setting name="lazyLoadingEnabled" value="true"/>
       <setting name="multipleResultSetsEnabled" value="true"/>
       <setting name="useColumnLabel" value="true"/>
       <setting name="useGeneratedKeys" value="false"/>
       <setting name="autoMappingBehavior" value="PARTIAL"/>
       <setting name="autoMappingUnknownColumnBehavior" value="WARNING"/>
       <setting name="defaultExecutorType" value="SIMPLE"/>
       <setting name="defaultStatementTimeout" value="25"/>
       <setting name="defaultFetchSize" value="100"/>
       <setting name="safeRowBoundsEnabled" value="false"/>
       <setting name="mapUnderscoreToCamelCase" value="false"/>
       <setting name="localCacheScope" value="SESSION"/>
       <setting name="jdbcTypeForNull" value="OTHER"/>
       <setting name="lazyLoadTriggerMethods" value="equals,clone,hashCode,toString"/>
     </settings>
     ```

3. 类型别名（`typeAliases`）

   为Java类型建立别名，一般使用更短的名称替代

   ```xml
   <typeAliases>
     <typeAlias alias="Author" type="domain.blog.Author"/>
     <typeAlias alias="Blog" type="domain.blog.Blog"/>
     <typeAlias alias="Comment" type="domain.blog.Comment"/>
     <typeAlias alias="Post" type="domain.blog.Post"/>
     <typeAlias alias="Section" type="domain.blog.Section"/>
     <typeAlias alias="Tag" type="domain.blog.Tag"/>
   </typeAliases>
   ```

4. 类型处理器（`typeHanders`）

    用于将预编译语句（`PreparedStatement`）或结果集（`ResultSet`）中的JDBC类型转化成Java 类型

    1. 如：`BooleanTypeHandler`  将 JDBC类型中的BOOLEAN转化成Java类型中的java.lang.Boolean 或者 boolean。

    2. 若需要转换 Java 8 新增的Date与Time API，即JSR-310，需要再引入mybatis-typehandlers-jsr310

      ```xml
      <dependency> 	
           <groupId>org.mybatis</groupId> 	
           <artifactId>mybatis-typehandlers-jsr310</artifactId>	 
           <version>1.0.2</version>
      </dependency>
      ```


You can override the type handlers or create your own to deal with unsupported or non-standard types. To do so, implement the interface `org.apache.ibatis.type.TypeHandler` or extend the convenience class `org.apache.ibatis.type.BaseTypeHandler` and optionally map it to a JDBC type. For example:

```java
// ExampleTypeHandler.java
@MappedJdbcTypes(JdbcType.VARCHAR)
public class ExampleTypeHandler extends BaseTypeHandler<String> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i,
                                    String parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter);
    }

    @Override
    public String getNullableResult(ResultSet rs, String columnName)
        throws SQLException {
        return rs.getString(columnName);
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex)
        throws SQLException {
        return rs.getString(columnIndex);
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex)
        throws SQLException {
        return cs.getString(columnIndex);
    }
}

<!-- mybatis-config.xml -->
<typeHandlers>
  <typeHandler handler="org.mybatis.example.ExampleTypeHandler"/>
</typeHandlers>
```

5. 对象工厂（`objectFactory`）

   用于创建结果对象实例，提供默认构造器或者执行构造参数初始化目标类型的对象。通常使用场景，不需要调整默认的实现。

```Java
// ExampleObjectFactory.java
public class ExampleObjectFactory extends DefaultObjectFactory {
    public Object create(Class type) {
        return super.create(type);
    }
    public Object create(Class type, List<Class> constructorArgTypes, List<Object> constructorArgs) {
        return super.create(type, constructorArgTypes, constructorArgs);
    }
    public void setProperties(Properties properties) {
        super.setProperties(properties);
    }
    public <T> boolean isCollection(Class<T> type) {
        return Collection.class.isAssignableFrom(type);
    }
}

<!-- mybatis-config.xml -->
<objectFactory type="org.mybatis.example.ExampleObjectFactory">
  <property name="someProperty" value="100"/>
</objectFactory>

```

6. 插件（`plugins`）

   `Mybatis`提供插件的方式来拦截映射语句（mapped statement）的执行，如以下方法:

   1. `Executor` (update, query, flushStatements, commit, rollback, getTransaction, close, isClosed)
   2. `ParameterHandler` (getParameterObject, setParameters)
   3. `ResultSetHandler` (handleResultSets, handleOutputParameters)
   4. `StatementHandler` (prepare, parameterize, batch, update, query)

```Java
// ExamplePlugin.java
@Intercepts({@Signature(
    type= Executor.class,
    method = "update",
    args = {MappedStatement.class,Object.class})})
public class ExamplePlugin implements Interceptor {
    public Object intercept(Invocation invocation) throws Throwable {
        return invocation.proceed();
    }
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }
    public void setProperties(Properties properties) {
    }
}

<!-- mybatis-config.xml -->
<plugins>
  <plugin interceptor="org.mybatis.example.ExamplePlugin">
    <property name="someProperty" value="100"/>
  </plugin>
</plugins>
```

7. 环境（environments）

   MyBatis 允许配置多个环境，在运行时，通过传递环境信息，切换关联的SqlSessionFactory 实例。因此，MyBatis 中的环境（environments）类似于Maven 或者 Spring 中的Profile。

```xml
<environments default="development">
  <environment id="development">
    <transactionManager type="JDBC">
      <property name="..." value="..."/>
    </transactionManager>
    <dataSource type="POOLED">
      <property name="driver" value="${driver}"/>
      <property name="url" value="${url}"/>
      <property name="username" value="${username}"/>
      <property name="password" value="${password}"/>
    </dataSource>
  </environment>
</environments>
```

> 这里使用type来指定对应的实现类 POOLED --> PooledDataSource  , JDB

1. 数据库标识提供商（databaseIdProvider）

   MyBatis 是面向SQL的映射框架，所执行SQL语句的语法依赖于数据库提供商的实现，比如：MySQL、Oracle、SQL Server等。在配置映射SQL语句时，可为其指定具体的     数据库提供商的实现。因此，在全局XML配置文件中可以定义多个数据库标识供应器（databaseIdProvider）

```xml
<databaseIdProvider type="DB_VENDOR">
  <property name="SQL Server" value="sqlserver"/>
  <property name="DB2" value="db2"/>
  <property name="Oracle" value="oracle" />
</databaseIdProvider>
```

2. SQL映射文件（mappers）

```xml
<!-- Using classpath relative resources -->
<mappers>
  <mapper resource="org/mybatis/builder/AuthorMapper.xml"/>
  <mapper resource="org/mybatis/builder/BlogMapper.xml"/>
  <mapper resource="org/mybatis/builder/PostMapper.xml"/>
</mappers>

<!-- Using url fully qualified paths -->
<mappers>
  <mapper url="file:///var/mappers/AuthorMapper.xml"/>
  <mapper url="file:///var/mappers/BlogMapper.xml"/>
  <mapper url="file:///var/mappers/PostMapper.xml"/>
</mappers>

<!-- Using mapper interface classes -->
<mappers>
  <mapper class="org.mybatis.builder.AuthorMapper"/>
  <mapper class="org.mybatis.builder.BlogMapper"/>
  <mapper class="org.mybatis.builder.PostMapper"/>
</mappers>

<!-- Register all interfaces in a package as mappers -->
<mappers>
  <package name="org.mybatis.builder"/>
</mappers>
```
## MyBatis 实例
### MyBatis 配置

1. XML定义

   - 文档类型约束方式

     DTD：Document Type Definitionhttp://mybatis.org/dtd/mybatis-3-config.dtd

   - 子元素

     properties, settings, typeAliases, typeHandlers, objectFactory, objectWrapperFactory, reflectorFactory, plugins, environments, databaseIdProvider, mappers

2. API接口

   	`org.apache.ibatis.session.Configuration`

#### 属性（properties）

1. 配置内容

    `<properties resource="mybatis/mybatis.properties"/>`

2. 组装Api接口

    `org.apache.ibatis.session.Configuration`#variables

3. 填充配置

    `org.apache.ibatis.builder.xml.XMLConfigBuilder`(XPathParser,String, Properties)

#### 设置（settings）

1. XML声明

   	<settings> 元素
      `<setting> `元素

2. 组装API接口

    `org.apache.ibatis.session.Configuration`#setXXX(*)

3. 填充配置

    `org.apache.ibatis.builder.xml. XMLConfigBuilder`#settingsElement(Properties)


#### 类型别名（typeAliases）

1. XML声明

   `<typeAliases>` 元素 

   	`<typeAliase>` 元素

2. 组装API接口

    `org.apache.ibatis.session.Configuration`#typeAliasRegistry

3. API定义

    `org.apache.ibatis.type.TypeAliasRegistry`

4. 填充配置

    `org.apache.ibatis.builder.xml.XMLConfigBuilder`#typeAliasesElement(XNode)

#### 类型处理器（typeHanders）

1. XML声明

   `<typeHanders>` 元素

   	`<typeHander>` 元素

2. 组装API接口

    `org.apache.ibatis.session.Configuration`#typeHandlerRegistry

3. API定义

    `org.apache.ibatis.type.TypeHandlerRegistry`

4. 填充配置

    `org.apache.ibatis.builder.xml.XMLConfigBuilder`#typeHandlerElement (XNode)

#### 对象工厂（objectFactory）

1. XML声明

   `<objectFactory>` 元素

   	`<properties>` 元素

2. 组装API接口

    `org.apache.ibatis.session.Configuration#setObjectFactory`

3. 填充配置

    `org.apache.ibatis.builder.xml.XMLConfigBuilder`#objectFactoryElement(XNode)

#### 插件（plugins）

1. XML声明
   `<plugins>`元素

   	`<plugin>` 元素

2. 组装API接口

    `org.apache.ibatis.session.Configuration`#getInterceptors

3. 填充配置

    `org.apache.ibatis.builder.xml.XMLConfigBuilde`r#pluginElement(XNode)

#### 环境（environments）

1.XML声明

`<environments>`元素

	`<environment>` 元素

1. 组装API接口

    `org.apache.ibatis.session.Configuration`#setEnvironment

2. 填充配置

    `org.apache.ibatis.builder.xml.XMLConfigBuilder`#environmentsElement(XNode)

#### 数据库标识提供商（databaseIdProvider）

1. XML声明

    `<databaseIdProvider>`元素

    	`<property>` 元素

2. 组装API接口

    `org.apache.ibatis.session.Configuration`#setDatabaseId

3. 填充配置

    `org.apache.ibatis.builder.xml.XMLConfigBuilder`#databaseIdProviderElement(XNode)

#### SQL映射文件（mappers）

1. XML声明

   `<mappers> `元素

   	`<mapper>` 元素

2. 组装API接口

    `org.apache.ibatis.session.Configuration`#mapperRegistry

3. API定义

    `org.apache.ibatis.type.MapperRegistry`

4. 填充配置

    `org.apache.ibatis.builder.xml.XMLConfigBuilder`#mapperElement(XNode)

### MyBatis 核心API

#### org.apache.ibatis.session.SqlSessionFactoryBuilder

1. SqlSessionFactory 构建器，创建 SqlSessionFactory 实例。通过重载方法build，控制实例行为，其中方法参数如下：

   - MyBatis全局配置流（java.io.InputStream、java.io.Reader）
   - Mybatis环境名称（environment）
   - Mybatis属性（java.util.Properties）
2. 相关API
   - 配置构建器：`org.apache.ibatis.builder.xml.XMLConfigBuilderMyBatis`
   - 配置：`org.apache.ibatis.session.Configuration`
   - MyBatis环境：`org.apache.ibatis.mapping.Environment`

#### org.apache.ibatis.session.SqlSessionFactory

SqlSession 工厂类，创建 SqlSession 实例，通过重载方法openSession，控制实例特性，其中方法参数如下：

- 是否需要自动提交
- JDBC 数据库连接（`java.sql.Connection`）
- Mybatis SQL语句执行器类型（`org.apache.ibatis.session.ExecutorType`）
- Mybatis 事务隔离级别（`org.apache.ibatis.session.TransactionIsolationLevel`）

#### org.apache.ibatis.session.SqlSession

MyBatis SQL 会话对象，类似于JDBC Connection

- 封装java.sql.Connection
- 屏蔽java.sql.Statement（以及派生接口）的细节
- 映射java.sql.ResultSet 到Java类型
- 事务控制
- 缓存
- 代理映射（Mapper）


## MyBatis 自动化工具

> 资源地址：http://www.mybatis.org/generator/index.html
