import { MdInbox } from "react-icons/md";
import { IoIosSend } from "react-icons/io";
import { CiTrash } from "react-icons/ci";
import {useParams} from 'react-router-dom'
import {useNavigate} from 'react-router-dom'

function SideBar(){
    const params = useParams();
    const navigate = useNavigate();
    return (
        <div className={"side-bar"}>

            <button className={"btn"} onClick={()=> { navigate("/" + params.user +"/send")}}>
                <IoIosSend style={{fontSize: "1.7rem"}}/>
                 Send
            </button>

            <button className={"btn"} onClick={()=> { navigate("/" + params.user +"/inbox")}}>
                <MdInbox style={{fontSize: "1.7rem"}}/>
                Inbox
            </button>
            <button className={"btn"}>
                <CiTrash style={{fontSize: "1.7rem"}}/>
                Trash
            </button>


        </div>


    )
}

export default SideBar;