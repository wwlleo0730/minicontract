pragma solidity ^0.4.0;

contract MiniContract{

    address delegate;

    function MiniContract () public {
        delegate = msg.sender;
    }

    modifier access() {
        if(msg.sender != delegate){
            revert();
        }
        _;
    }

    struct minicontract{
        bytes A;
        bytes B;
        bytes Aphone;
        bytes Bphone;
        bool finished;
        bytes title;
        bytes content;
        bytes soundAddr;
        bytes pictureAddr;
    }

    mapping(uint256 => minicontract) db;
    uint256 dbindex;//index to last Non-Used cardinal number

    mapping(bytes => uint256[]) owns;

    event eMint(uint256 dbindex);
    event eComplete(uint256 dbindex);

    function mint(bytes _A, bytes _B, bytes _Aphone, bytes _Bphone, bytes _title, bytes _content, bytes _sound, bytes _picture) access public{
        minicontract memory temp = minicontract(_A,_B,_Aphone,_Bphone,false,_title,_content,_sound,_picture);
        db[dbindex] = temp;

        owns[_A].push(dbindex);
        owns[_B].push(dbindex);
        eMint(dbindex);

        dbindex++;
    }

    function complete(uint256 _dbindex) access public{
        db[_dbindex].finished = true;
        eComplete(_dbindex);
    }

    function list(bytes _owner) public view returns(uint256[]){
        return owns[_owner];
    }

    function detailMeta(uint256 _dbindex) public view returns(bytes _A, bytes _B, bytes _Aphone, bytes _Bphone, bool _finished){
        _A = db[_dbindex].A;
        _B = db[_dbindex].B;
        _Aphone = db[_dbindex].Aphone;
        _Bphone = db[_dbindex].Bphone;
        _finished = db[_dbindex].finished;
    }

    function detailContent(uint256 _dbindex) public view returns(bytes _title, bytes _content, bytes _sound, bytes _picture){
        _title = db[_dbindex].title;
        _content = db[_dbindex].content;
        _sound = db[_dbindex].soundAddr;
        _picture = db[_dbindex].pictureAddr;
    }
}