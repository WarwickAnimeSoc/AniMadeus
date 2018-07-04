### aniMadeus
aniMadeus is a Discord Bot written in Java for use on the University of Warwick Student Union's Anime and Manga society. It's main purpose is the validation of user information. It is currently work in progress and is fairly limited in overall features. Currently maintained by MrPorky, with aid from the prior web-masters.

### Installation:
Installing aniMadeus is fairly simple. First, you should clone this repo: \
`git clone https://github.com/WarwickAnimeSoc/aniMadeus` \
Then after completing this, it is important to download the dependencies for the project. In this case, aniMadeus requires:
* JDA
* cassandra-all
* mysql-connector-java \

These dependecies can be downloaded manually, but for ease of use, it is preferable to use Apache Maven to copy of these files. Inside of the `pom.xml`, make sure that the following dependencies are specified:
```xml
	<repositories>
        <repository>
            <id>jcenter</id>
            <name>jcenter-bintray</name>
            <url>http://jcenter.bintray.com</url>
        </repository>
    </repositories>

    <dependencies>
        <dependency>
            <groupId>net.dv8tion</groupId>
            <artifactId>JDA</artifactId>
            <version>3.6.0_376</version>
        </dependency>
        <dependency>
            <groupId> org.apache.cassandra</groupId>
            <artifactId>cassandra-all</artifactId>
            <version>0.8.1</version>
            <exclusions>
                <exclusion>
                    <groupId>org.slf4j</groupId>
                    <artifactId>slf4j-log4j12</artifactId>
                </exclusion>
                <exclusion>
                    <groupId>log4j</groupId>
                    <artifactId>log4j</artifactId>
                </exclusion>
            </exclusions>
        </dependency>

        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>6.0.6</version>
        </dependency>
    </dependencies>
 ```
### Creating and running a Bot instance:
To create and run a bot instance first navigate to [Discord developers](https://discordapp.com/developers/applications/me), and create a new Bot application. After making sure that the new application is a bot user, create a new token and copy this over into the configuration file. This file should look like this:
```
DISCORD-KEY [TOKEN]
SU-API [WARWICK_SU_KEY]
DBHOSTNAME [DB i.e. localhost]
DBNAME [name of DB]
DBUSERNAME [username]
DBPASSWORD [password]
```
Replace [TOKEN] with whatever your Discord developer token is. For any future webmasters, make sure that the SU-API key is valid, as sections of the bot will not work in the event of the key failing. \
From this point, you can run InitMain, which will launch the bot.
