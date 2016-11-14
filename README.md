# phoenix
The art of ancient.


使用已有的sqlite数据库文件
http://www.reigndesign.com/blog/using-your-own-sqlite-database-in-android-applications/
1. 把准备好的数据库文件放在目录/project_name/module_name/src/main/assets/中
2. 继承SQLiteOpenHelper创建一个Database Helper类
   * 实现onCreate()和onUpgrade()方法
   * 提供一个构造函数，在里面调用父类的构造函数，传入DB_NAME和DB_VERSION
   
   
数据库设计：
多级目录：    http://mikehillyer.com/articles/managing-hierarchical-data-in-mysql/
SELECT t1.name AS lev1, t2.name as lev2, t3.name as lev3, t4.name as lev4
FROM menu AS t1
LEFT JOIN menu AS t2 ON t2.parent_id = t1._id
LEFT JOIN menu AS t3 ON t3.parent_id = t2._id
LEFT JOIN menu AS t4 ON t4.parent_id = t3._id
WHERE t3._id = 20;

SELECT p._id AS lev1, p.title as lev2, c1.name as lev3, c2.name as lev4
FROM poems AS p
JOIN category AS c1 ON c1._id = p.category_id
LEFT JOIN category AS c2 ON c1.parent_id = c2._id

悬停标题
https://github.com/davideas/FlexibleAdapter/wiki/5.x-%7C-Headers-and-Sections
