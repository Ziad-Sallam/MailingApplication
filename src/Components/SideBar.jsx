import {MdEdit, MdInbox} from "react-icons/md";
import { IoIosSend } from "react-icons/io";
import { CiTrash } from "react-icons/ci";
import { useParams, useNavigate } from 'react-router-dom';
import { useEffect, useState } from "react";
import axios from "axios";
import {FaFolder} from "react-icons/fa";
import {RiDraftFill} from "react-icons/ri";

function SideBar() {
    const params = useParams();
    const navigate = useNavigate();
    const [folders, setFolders] = useState(["New folder", "New folder1"]);

    useEffect(() => {
        async function fetchFolders() {
            const param = {
                email: params.user,
            };
            try {
                const response = await axios.get("http://localhost:8080/api/users/getFolders", { params: param });
                setFolders(response.data);
                console.log(response.data)
            } catch (error) {
                console.error('Error fetching folders:', error);
            }
        }
        fetchFolders();
    }, [params.user]);

    return (
        <div className="side-bar">
            <button className="btn" onClick={() => navigate("/" + params.user + "/send")}>
                <MdEdit style={{fontSize: "1.7rem"}}/>
                Compose
            </button>
            <button className="btn" onClick={() => navigate("/" + params.user + "/folder/draft")}>
                <RiDraftFill style={{fontSize: "1.7rem"}}/>
                Draft
            </button>
            {folders.map((folder, index) =>

                (
                    <>
                        <button className="btn" key={index} onClick={() => {
                            navigate("/" + params.user + "/folder/" + folder);
                        }}>
                            {folder === "Trash" && <CiTrash style={{fontSize: "1.7rem"}}/>}
                            {folder === "Inbox" && <MdInbox style={{fontSize: "1.7rem"}}/>}
                            {folder === "Sent" && <IoIosSend style={{fontSize: "1.7rem"}}/>}
                            {folder !== "Trash" || folder !== "Inbox" || folder !== "Sent" && (
                                <FaFolder style={{fontSize: "1.7rem"}}/>)}
                            {folder}

                        </button>
                    </>)
            )}
        </div>
    );
}

export default SideBar;
