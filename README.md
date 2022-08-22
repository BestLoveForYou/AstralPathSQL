In the latest version of UPDATE, it will be solved in the next version
# AstralPathSQL
   Creator BestLoveForYou  

   website:[website](http://www.godserver.cn/)  
      
   email:yaoboyulove@163.com  
   
   If you are interested in this project, please give me a star!  
   2022-07-17 0:4 Beijing time
# Brief Introduction:
- This is a database system, completely based on java!
- The data structure used is a balanced binary tree.
- This data structure allows for efficient storage, insertion and deletion even with large amounts of data. 

# Usage
**Please do not use uppercase instructions!**

**Server**:   
**function:**   
Supported basis sql language   

other function:   
**User authentication**:    
The format is username: password. User data can be added and deleted on the server or on the client that has passed user authentication
Example: user add root:123456, the default user is root:123456
**Underlying code**:    
status - Get the number of connections, status, etc   
Cache function, user permissions, and better optimization  
**Client**:   
Enter IP and port number first, example: 127.0.0.1:9999
Then authenticate to use

# Config
The config of it (info.properties) include port , all_connect , and change_time   

# Update records
# v1.110 series 
 In this series, there have been a large range of changes, and other items except for the table class can still be supported downwards.
This version has a high probability of bugs (because the engine has changed)
 ## 1.116.20220814
 - Fixed the problem of incomplete data insertion
 - Fixed the problem of atomicity
 - Make the procedure more orderly
 ## 1.115.20220805-NatusVincere
 - log system
 - Phase final version, expected after 1 month next update
 - A " like " syntax to support searching
 - Fixed bug of update
 ## 1.114.20220803
 - Add new multi-database function
 - Greatly improved performance   
 **大家七夕节快乐吖**
 ## 1.113.20220801
 - Fixed a bug in the form.
 - Optimized program
 ## 1.112.20220729
 - Support remote shutdown / restart of database
 - Blocking, blocking IP and other new functions
 ## 1.111.20220728
 - Fixed the problem of data update and deletion
 - Optimized creation of tables
 - Optimized binary tree engine
 
 ## 1.110.20220727
 - Optimize your search engine
 - Fixed the failure of binary tree search, the default form search does not use binary tree, only when the form attribute is set to "1", can use binary tree engine
 - Fixed an issue with incorrect encryption
 - Greatly accelerated binary tree engine speed, speed up at least ten times
# v1.100 series
 
 ## 1.106.20220717
 - Updated the new function of startup parameters!
 ## 1.105.20220717
 - Provides a more secure encryption and decryption format
 - Fixed update failure
 - Better data processing, and better data compatibility
 - Fixed some small bugs
 ## 1.104.20220716
 - Fixed several bugs
 - Bypass authentication bug when user logs in
 - Failed Add when Data Add Incomplete
 - Multiple ";" when updating data;
 - Bug during accurate data search
 - Better login verification
 ## 1.103.20220715
- Several known bugs have been fixed. The biggest bugs are the execution of **update** and incomplete data
## v1.102.20220714
- New functions are available! Users need to log in to connect. The default login account and password are **root:123456**, and the format needs to be
Account Name: password.
 ## v1.101.20220714
Fixed known bugs, including the creation and use of data tables, SQL syntax, etc
 
 - **Epic update!!!**
  Change the syntax to SQL syntax! Compatible with some SQL languages Others are being manufacture!
  More support!!!
 ## v1.100.20220714 Date:2022-07-14

# v1.000 series
   ## v1.000.20220711
   - This is the original version, which provides binary tree and basic functions

   ## v1.000.20220713
   - Added cache function to improve query speed




