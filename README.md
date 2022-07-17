# AstralPathSQL
   Creator BestLoveForYou  
   
   website:[website](http://www.godserver.cn/)  
      
   email:yaoboyulove@163.com  
   
   2022-07-17 0:4 Beijing time
# Brief Introduction:
- This is a database system, completely based on java!
- The data structure used is a balanced binary tree.
- This data structure allows for efficient storage, insertion and deletion even with large amounts of data. 

# Usage
**Server**:   
**function:**   
Including select, insert, update and delete are supported!   
other function:   
**User authentication**:    
The format is username: password. User data can be added and deleted on the server or on the client that has passed user authentication
Example: user add root:123456, the default user is root:123456
**Underlying code**:    
getall - Get all data
status - Get the number of connections, status, etc
**Currently, functions are being developed:**   
Cache function, user permissions, and better optimization

**Client**:   
Enter IP and port number first, example: 127.0.0.1:9999
Then authenticate to use

# Config
The config of it (info.properties) include port , all_connect , and change_time   
You can change the config (such as the port) to change application's port .Warning: you need to close the program before changing the configuration file, otherwise it will be overwritten

# Update records

# v1.100 series
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


