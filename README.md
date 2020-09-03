### aniMadeus
aniMadeus is a Discord Bot written in Java for use on the University of Warwick Student Union's Anime and Manga society Discord server. \
Its main purpose is the validation of user information, though also provides functionality for a reaction-based role assignment system. \
It is currently a work in progress and is fairly limited in overall features. \
Currently maintained by MrPorky, with aid from the prior web-masters. \

### Installation:
Installing aniMadeus is fairly simple. First, you should clone this repo: \
`git clone https://github.com/WarwickAnimeSoc/aniMadeus` \
After completing this, it is important to download the dependencies for the project. 
In the default state, aniMadeus requires:
* JDA
* cassandra-all
* mysql-connector-java
* Lavaplayer
* fasterXML Jackson libraries

These dependencies can be downloaded manually, but for ease of use, 
we recommend that you use Apache Maven to download copies of these files. 
Inside of the `pom.xml`, make sure you specify the following dependencies:
```xml
	<repositories>
        <repository>
            <id>jcenter</id>
            <name>jcenter-bintray</name>
            <url>https://jcenter.bintray.com</url>
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
        
        <dependency>
            <groupId>com.sedmelluq</groupId>
            <artifactId>lavaplayer</artifactId>
            <version>1.2.58</version>
        </dependency>

        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-core</artifactId>
            <version>2.11.2</version>
        </dependency>
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-annotations</artifactId>
            <version>2.11.2</version>
        </dependency>
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>2.11.2</version>
        </dependency>

    </dependencies>
 ```
### Creating and running a Bot instance:
To create and run a bot instance first navigate to [Discord developers](https://discordapp.com/developers/applications/me), and create a new Bot application. 
After making sure that the new application is a bot user, create a new token and copy this over into the configuration file. 
This file should look like this:
```json
{
  "discordKey": "[TOKEN]",
  "suAPI": "[WARWICK SU API]",
  "dbHostName": "[HOST NAME]",
  "dbName": "[DB NAME]",
  "dbUsername": "[USERNAME]",
  "dbPassword": "[PASSWORD]",

  "channels": [
    {
      "channelId": [CHANNEL ID AS NUMBER],
      "roleReactPairs": [
        {"react": "1️⃣", "role": "general"},
        {"react": "2️⃣", "role": "art"},
        {"react": "3️⃣", "role": "misc"},
        {"react": "\uD83C\uDFB5", "role": "amq"},
        {"react": "\uD83C\uDF93", "role": "graduate"},
        {"react": "↖️", "role": "non-warwick"},
        {"react": "\uD83C\uDF99", "role": "vc"}
      ]
    }
  ]
}
```
Replace [TOKEN] with whatever your Discord developer token is. For any future webmasters, make sure that the SU-API key is valid, as sections of the bot will not work in the event of the key failing. \
From this point, you can run InitMain, which will launch the bot. \
An example of the roleReactPairs list has been provided, though the emotes do not show up properly in the README (from top to bottom they are :one:, :two:, :three:, :musical_note:, :mortar_board:, :arrow_upper_left:, :microphone2:).\
<b>To add new emotes, I would recommend uncommenting the printing statements in the `WelcomeReactionEventHandler.java` file and seeing what the exact representation of the emote is in order to enter it into the configuration file.</b>
