import PropTypes from "prop-types";
import { useState, useEffect, useRef } from "react";
import { useNavigate, useParams } from "react-router-dom";
import axios from "axios";
import { FiSearch } from "react-icons/fi";
import {MdOutlineEdit} from "react-icons/md";
import {RiDeleteBin6Line} from "react-icons/ri";

const mail = {
    sender: PropTypes.string,
    receiver: PropTypes.arrayOf(PropTypes.string),
    body: PropTypes.string,
    subject: PropTypes.string,
    date: PropTypes.string,
    id: PropTypes.number,
    priority: PropTypes.number // Include priority in PropTypes
};

ListBox.propTypes = {
    mails: PropTypes.arrayOf(PropTypes.shape(mail))
};

function ListBox() {
    const navigate = useNavigate();
    const params = useParams();
    const [folderName,setFolderName] = useState(params.folderName);

    const [mails, setMails] = useState([
        {
            sender: "aa",
            receiver: ["sccs"],
            body: "hello",
            subject: "zz",
            dateSent: "22/10/2024",
            id: 2,
            priority: 0
        },
        {
            sender: "zz",
            receiver: ["merer"],
            body: "zello World",
            subject: "aa",
            dateSent: "23/10/2024",
            id: 3,
            priority: 1,
            temp: ""
        }
    ]);

    const allMails = useRef([]);

    useEffect(() => {
        async function fetchMails() {
            const param = {
                email: params.user,
                folder: params.folderName
            };
            console.log(param);

            try {
                if (params.folderName === "draft") {
                    const { data } = await axios.get("http://localhost:8080/api/users/getDrafts", { params: param });
                    console.log("here");
                    console.log(data);
                    setMails(data);
                    allMails.current = data;

                    setMails(prevMails => prevMails.map(mail => ({
                        ...mail,
                        id: mail.temp
                    })));
                    console.log(mails);
                } else {
                    const { data } = await axios.get("http://localhost:8080/api/users/getEmails", { params: param });
                    console.log("here");
                    console.log(data);
                    const sorted = [...data].sort((a, b) => b.id - a.id);
                    setMails(sorted);
                    allMails.current = sorted;
                }
            } catch (error) {
                console.error('Error fetching emails:', error);
            }
        }

        fetchMails();
    }, [params.folderName, params.user]);

    function sort(e) {
        console.log(mails);
        let sorted;
        console.log(e.target.value);
        switch (e.target.value) {
            case "importance":
                sorted = [...mails].sort((a, b) => b.priority - a.priority);
                setMails(sorted);
                break;
            case "body":
                sorted = [...mails].sort((a, b) => a.body.toLowerCase().localeCompare(b.body.toLowerCase()));
                setMails(sorted);
                break;
            case "date":
                sorted = [...mails].sort((a, b) => b.id - a.id);
                setMails(sorted);
                break;
            case "subject":
                sorted = [...mails].sort((a, b) => a.subject.toLowerCase().localeCompare(b.subject.toLowerCase()));
                setMails(sorted);
                break;
            case "from":
                sorted = [...mails].sort((a, b) => a.sender.toLowerCase().localeCompare(b.sender.toLowerCase()));
                setMails(sorted);
                break;
            default:
                break;
        }
    }

    function search(e) {
        console.log(allMails.current);
        const searched = [...allMails.current].filter(mail => (mail.subject.toLowerCase().includes(e.target.value.toLowerCase()) || mail.sender.toLowerCase().includes(e.target.value.toLowerCase()) ||mail.body.toLowerCase().includes(e.target.value.toLowerCase())));
        console.log(searched)
        setMails(searched);
    }

    async function rename(){
        await axios.post(`http://localhost:8080/api/users/renameFolder/${params.user}/${params.folderName}/${folderName}`)

        navigate("/" + params.user + "/folder/" + folderName);


    }
    async function deleteFolder(){
        await axios.post(`http://localhost:8080/api/users/deleteFolder/${params.folderName}/${params.user}`)
        navigate("/" + params.user + "/folder/inbox");
        window.location.reload();

    }
    function enableEdit(){
        return(<div className={"list-title"}>

            <div style={{display: "flex"}}><input className={"folder-name"} type={"text"} value={folderName}
                                                  onChange={(e) => setFolderName(e.target.value)}/>
                <button className={"btn btn-lg"} style={{marginLeft: "20px"}} onClick={rename}><MdOutlineEdit
                    style={{fontSize: "2.5rem"}}/></button>
            </div>


            <button className={"btn btn-lg"} onClick={deleteFolder}><RiDeleteBin6Line style={{fontSize: "2.5rem"}}/>
            </button>

        </div>)
    }
    function disableEdit() {
        return (<div className={"list-title"}>

            <div style={{display: "flex"}}>
                <h1 style={{fontSize: "3rem", fontWeight : "normal"}}>{folderName}</h1>
            </div>

        </div>)

    }

    return (
        <div className="list-box">

            {(folderName ==="Inbox" || folderName === "Sent" || folderName === "Trash") ? disableEdit() : enableEdit()}

            <hr/>
            <label>{"Sort by:  "}</label>
            <select id="sort-dropdown" onChange={sort}>
                <option value="date">Date</option>
                <option value="subject">Subject</option>
                <option value="body">Body</option>
                <option value="importance">Importance</option>
                <option value="from">From</option>
            </select>
            <label style={{marginLeft: "10%"}}><FiSearch style={{fontSize: "1.5rem"}}/></label>
            <input className={"search-input"} type={"text"} placeholder={"search..."} onChange={search}/>
            <table className="table table-striped list-box-table table-hover">
                <thead>
                <tr>
                    <th></th>
                    <th>From</th>
                    <th>Subject</th>
                    <th>Date</th>
                    <th>Priority</th>
                </tr>
                </thead>
                <tbody>
                {mails.map((mail, index) => (
                    <tr key={index}>
                        <td><input type={"checkbox"}/></td>
                        <td onClick={() => {
                            navigate("/" + params.user + "/" + (params.folderName === "draft" ? "" : params.folderName + "/") + (mail.id === 0 ? ('draft/' + mail.temp) : mail.id));
                        }}>{mail.sender}</td>
                        <td onClick={() => {
                            navigate("/" + params.user + "/" + (params.folderName === "draft" ? "" : params.folderName + "/") + (mail.id === 0 ? ('draft/' + mail.temp) : mail.id));
                        }}>{mail.subject}</td>
                        <td>{mail.dateSent}</td>
                        <td>{mail.priority}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}

export default ListBox;
