
使用已有的sqlite数据库文件
http://www.reigndesign.com/blog/using-your-own-sqlite-database-in-android-applications/
1. 把准备好的数据库文件放在目录/project_name/module_name/src/main/assets/中
2. 继承SQLiteOpenHelper创建一个Database Helper类
   * 实现onCreate()和onUpgrade()方法
   * 提供一个构造函数，在里面调用父类的构造函数，传入DB_NAME和DB_VERSION