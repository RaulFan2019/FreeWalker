/* Prefix 
-----------------------------
| 0xFE:1 | 0x95: 1 | length |
-----------------------------
*/

// port
public let kTextMessage = 1
public let kVoiceMessage = 2
public let kQueryMessage = 3

// type
public let kGroupChat = 1
public let kSingalChat = 2
public let kLocationInfoQuery = 3
public let kLocationInfo = 4
public let kUserInfoQuery = 5
public let kUserInfo = 6
public let kUsername = 7

// message public format head
var buf = [UInt8]()

buf.append(0xFE)   // prefix
buf.append(0x95)   
buf.append(UInt8(payload.count + 2))  // length
buf.append(port)       // port
buf.append(type)        // type

buf.append(contentsOf: payload)

// text messages
// group message
/*
-----------------------------
| uid: 4 | content: 1 ~ 200 | 
-----------------------------
*/

let uid = // my user id 
    
let data = content.data(using: .utf8) // content: string

let bytes = [UInt8](data!)
let lowerByte = UInt8(UInt(uid)! & 0xff)
let lowByte = UInt8((UInt(uid)! >> 8) & 0xff)
let highByte = UInt8((UInt(uid)! >> 16) & 0xff)
let higherByte = UInt8((UInt(uid)! >> 24) & 0xff)

var buffer = [UInt8]()
buffer.append(higherByte)
buffer.append(highByte)
buffer.append(lowByte)
buffer.append(lowerByte)
buffer.append(contentsOf: bytes)


// singal message
/*
----------------------------------------------
| uid: 4 | dest userid: 4 | content: 1 ~ 200 | 
----------------------------------------------
*/


// port = query message 
// 包括查询locaiton，查询user info，查询username等

if (port == kQueryMessage) {
    switch type {
    case UInt8(kLocationInfoQuery):
    	// 收到查询信息，发出自己的gps 和 info message
        let channel = bytes[1]
        sendGPSAndInfoMessage(channel: Int(channel))
    case UInt8(kLocationInfo):
        // 收到某人的location info，保存在本地
    	// 本地需要有所有已经发过信息的人的信息，暂时数量未定
        
    case UInt8(kUserInfoQuery):
        // 收到对端发起的user info query，发送自己的info
        sendUserInfoMessage()
    case UInt8(kUserInfo):
        // 收到某人的user info， 保存到本地
       
    default:
        print("handle message default")
    }
    
}

// sendGPSAndInfoMessage
// user info, 都发送我们约定的内容index
/*
---------------------------------------------------------------------------------------------------------------------------------------
| channel: 1 | userid: 4 | latitude: 4 | longtitude: 4 | gender: 1 | age: 1 | sex: 1 | job: 1 | height: 1 | weight: 1 | username: 1~8 |
---------------------------------------------------------------------------------------------------------------------------------------
*/
	let uid = UserDefaults.standard.string(forKey: UserKeyClass.AccountInfo().uid)!
    let lowerByte = UInt8(UInt(uid)! & 0xff)
    let lowByte = UInt8((UInt(uid)! >> 8) & 0xff)
    let highByte = UInt8((UInt(uid)! >> 16) & 0xff)
    let higherByte = UInt8((UInt(uid)! >> 24) & 0xff)

    let latitude =  UserDefaults.standard.double(forKey: UserKeyClass.LocationInfo().latitude)
    let longtitude = UserDefaults.standard.double(forKey: UserKeyClass.LocationInfo().longitude)
    
    let latitudeInt = (latitude + 90) * 1000000
    let longtitudeInt = (longtitude + 180) * 1000000
    
    // INFO
    let gender = UInt8(UserDefaults.standard.integer(forKey: UserKeyClass.AccountInfo().gender))
    let age = UInt8(UserDefaults.standard.integer(forKey: UserKeyClass.AccountInfo().age))
    let sex = UInt8(UserDefaults.standard.integer(forKey: UserKeyClass.AccountInfo().sex))
    let job = UInt8(UserDefaults.standard.integer(forKey: UserKeyClass.AccountInfo().job))
    let height = UInt8(UserDefaults.standard.integer(forKey: UserKeyClass.AccountInfo().height))
    let weight = UInt8(UserDefaults.standard.integer(forKey: UserKeyClass.AccountInfo().weight))
    
    let username = UserDefaults.standard.string(forKey: UserKeyClass.AccountInfo().nickname)
    let data = username!.data(using: .utf8)
    let bytes = [UInt8](data!)
    
    // latitude longtitude = 4 bytes
    
    var buffer = [UInt8]()
    buffer.append(UInt8(channel))
    buffer.append(higherByte)
    buffer.append(highByte)
    buffer.append(lowByte)
    buffer.append(lowerByte)
    
    for i in 0..<4 {
        let loop = 3
        let temp = UInt(latitudeInt) >> (8*(loop - i)) & 0xFF
        buffer.append(UInt8(temp))
    }
    
    for i in 0..<4 {
        let loop = 3
        let temp = UInt(longtitudeInt) >> (8*(loop - i)) & 0xFF
        buffer.append(UInt8(temp))
    }
    
    buffer.append(gender)
    buffer.append(age)
    buffer.append(sex)
    buffer.append(job)
    buffer.append(height)
    buffer.append(weight)
    
    buffer.append(contentsOf: bytes)


// sendUserInfoMessage
    /*
--------------------------------------------------------------------------------------------
| userid: 4 | gender: 1 | age: 1 | sex: 1 | job: 1 | height: 1 | weight: 1 | username: 1~8 |
--------------------------------------------------------------------------------------------
*/
    