import {MdContacts, MdEdit, MdInbox} from "react-icons/md";
import { IoIosSend } from "react-icons/io";
import { CiTrash } from "react-icons/ci";
import { useParams, useNavigate } from 'react-router-dom';
import { useEffect, useState } from "react";
import axios from "axios";
import {FaFolder, FaRegFolder} from "react-icons/fa";
import {RiDraftFill, RiFolderAddLine} from "react-icons/ri";
import AddFolder from "./AddFolder.jsx";
import {CgProfile} from "react-icons/cg";

function SideBar() {
    const params = useParams();
    const navigate = useNavigate();
    const [folders, setFolders] = useState(["New folder", "New folder1"]);
    const [addFolder,setAddFolder] = useState(false);

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
        <div className={"main-container"}>

            <div className="side-bar">
                {/*<div className={"nav"}>*/}
                {/*    <CgProfile style={{margin: "10px", fontSize: "3rem"}}/>*/}
                {/*    <p>{params.user}</p>*/}
                {/*</div>*/}

                <hr/>
                <button className={"btn btn-lg btn-outline-danger"}
                        onClick={() => navigate("/" + params.user + "/send")}>
                    <MdEdit style={{fontSize: "1.7rem", marginRight: "10px"}}/>
                    Compose
                </button>
                <hr/>
                <button className="btn" onClick={() => {
                    navigate("/" + params.user + "/folder/draft");
                    window.location.reload();
                }}>
                    <RiDraftFill style={{fontSize: "1.7rem", marginRight: "10px"}}/>
                    Draft
                </button>
                {folders.map((folder, index) =>
                    (
                        <>
                            <button className="btn" key={index} onClick={() => {

                                navigate("/" + params.user + "/folder/" + folder);
                                window.location.reload();
                            }}>
                                {folder === "Trash" && <CiTrash style={{fontSize: "1.7rem", marginRight: "10px"}}/>}
                                {folder === "Inbox" && <MdInbox style={{fontSize: "1.7rem", marginRight: "10px"}}/>}
                                {folder === "Sent" && <IoIosSend style={{fontSize: "1.7rem", marginRight: "10px"}}/>}
                                {(folder !== "Trash" && folder !== "Inbox" && folder !== "Sent") && (
                                    <FaRegFolder style={{fontSize: "1.7rem", marginRight: "10px"}}/>)}
                                {folder}

                            </button>
                        </>)
                )}
                <button className="btn" onClick={() => navigate("/" + params.user + "/contact")}>
                    <MdContacts style={{fontSize: "1.7rem"}}/>
                    Contacts
                </button>
                <button className="btn" onClick={() => (setAddFolder(!addFolder))}>
                    <RiFolderAddLine style={{fontSize: "1.7rem"}}/>
                    Add Folders
                </button>
                {addFolder && <AddFolder onClick={() => (setAddFolder(!addFolder))}/>}
            </div>
        </div>

    );
}

export default SideBar;
