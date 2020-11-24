
/* Prefix 
-----------------------------
| 0xFE:1 | 0x95: 1 | length |
-----------------------------
*/

/* 所有报文已Prefix开头 */

/* Control message format*/

/*
Port 100: Fetch system status
-------
|N/A:0| -- non parameter
-------
result:
----------------------------------------------------------------------------------------------------------------------------------------
| data version:1 | Currrent Channel:1 | Priority:1 | TX Power:1 | Reserved:4 | Max Channel:1 | Channel Map:7 | RX Count:4 | TX Count:4 |
----------------------------------------------------------------------------------------------------------------------------------------

P.S. 所有的设备连接后，从设备获取信息，同步到APP的保存信息，所有设备配置以设备为准

e.g.
write:
[FE 95 1 64]

result:
[1, 1, 1, 0, 0, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0, 0, 141, 14, 0, 0, 61, 6, 0, 0]

Port 101: Set Channel & priority & cipherkey

---------------------------------------------
| Channel idx:1 | Priority:1 | CipherKey:16 |
---------------------------------------------
result
------------------------------
| Channel idx:1 | Priority:1 |
------------------------------

P.S. 选择Channel，配置，priority 为1~6十进制数，默认为5；如果有pass，则需加上CiperKey，CiperKey为16字节，目前最简单的处理，app配置时6位数字，前面加5个0，后面加5个0，凑成16个字节，下发


Port 102: Set tx power & channel max
--------------------
| txpower:1 |
--------------------
---------------------------------
| txpower:1 | channel max:1 |
---------------------------------
result:
-------------
| Retsult:1 | - current pri
-------------

P.S. Tx Power在my device页面增加了远距离模式，打开写22，关闭写17，channel max 目前耳机都配成30，暂不需修改，写30下发就行


Port 103: Get Device info
---------
| N/A:0 | -- non parameter
---------
---------------------
| UsrID:4 | AppID:4 |
---------------------
result:
---------------------------------------
| EUI:8 | UsrID:4 | AppID:4 | FmVer:2 |
---------------------------------------

P.S. 写好接口，暂时不需要调用


Port 104: Get/Set Device name
-------
|N/A:0| -- non parameter
-------
------------------
|name: 1~16 bytes|
------------------
result:
----------------------------
| name: string, 1~16 bytes |
----------------------------

P.S. 当用户配置了自己的Device name后，用此命令写给device，同步的时候，需要读出来，因为可能有人借设备，设备的名字需要同步给APP


Port 120: Retrieve channle's AES key, for debug only
-------
|N/A:0| -- none to retrieve current channel
-------
-------------------
| Channel idx:1 |
-------------------
result:
--------------
| AES Key:16 |
--------------

P.S. Debug Only

Port 126: switch to local file voice

P.S. Debug Only

Port 127: Reset to factory
-------
|N/A:0| -- non parameter
-------
*/



