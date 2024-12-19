import PropTypes from "prop-types";
import {useEffect, useRef, useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
import axios from "axios";
import {FiSearch} from "react-icons/fi";
import {MdOutlineEdit} from "react-icons/md";
import {RiDeleteBin6Line} from "react-icons/ri";
import {CiTrash} from "react-icons/ci";
import {IoMdCheckmark} from "react-icons/io";
import {SlReload} from "react-icons/sl";
import ErrorMsg from "./ErrorMsg.jsx";

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
    const [folderName, setFolderName] = useState(params.folderName);
    const [filterMode,setFilterMode] = useState("Sender");
    const [filterValue, setFilterValue] = useState("");
    const [numberOfPages, setNumberOfPages] = useState([1]);
    const [isFiltered, setIsFiltered] = useState(false);
    const [sortStrategy,setSortStrategies] = useState("date")
    const [currentPage, setCurrentPage] = useState(1);
    const [errorMsg, setErrorMsg] = useState(false)
    const [error,setError] = useState('')

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
    const [selectedFolder, setSelectedFolder] = useState("")

    const [folders, setFolders] = useState(["New folder", "New folder1"]);

    useEffect(() => {
        async function fetchMails() {
            const getNumberOfPagesParams={
                foldername: params.folderName,
            }
            if(params.folderName !=="draft"){
                const response = await axios.get(`http://localhost:8080/api/users/numberPages/${params.user}`,{params: getNumberOfPagesParams});
                const n = response.data
                console.log("n =" + n)
                setNumberOfPages(Array(n).fill(1))
            }

            const getPageParams = {
                page: 1,
                foldername: params.folderName,
                strategy: "",
                isFiltered: false,
                filterType: "",
                filterValue: "",
            }
            if(params.folderName ==="draft"){
                const param = {
                    email: params.user,
                    folder: params.folderName
                };

                        const {data} = await axios.get("http://localhost:8080/api/users/getDrafts", {params: param});
                        console.log("here");
                        console.log(data);
                        setMails(data);
                        allMails.current = data;

                        setMails(prevMails => prevMails.map(mail => ({
                            ...mail,
                            id: mail.temp
                        })));
                        console.log(mails);

            }
            else{
                const res = await axios.get(`http://localhost:8080/api/users/emailPages/${params.user}`,{params: getPageParams});
                setMails(res.data)
                console.log(res.data)

            }



            //
            const param = {
                email: params.user,
                folder: params.folderName
            };
            // console.log(param);
            //
            // try {
            //     if (params.folderName === "draft") {
            //         const {data} = await axios.get("http://localhost:8080/api/users/getDrafts", {params: param});
            //         console.log("here");
            //         console.log(data);
            //         setMails(data);
            //         allMails.current = data;
            //
            //         setMails(prevMails => prevMails.map(mail => ({
            //             ...mail,
            //             id: mail.temp
            //         })));
            //         console.log(mails);
            //     } else {
                    const {data} = await axios.get("http://localhost:8080/api/users/getEmails", {params: param});
                    console.log("here");
                    console.log(data);
            allMails.current = [...data].sort((a, b) => b.id - a.id);
            //     }
            // } catch (error) {
            //     console.error('Error fetching emails:', error);
            // }
            const paramf = {
                email: params.user,
            };
            try {
                const response = await axios.get("http://localhost:8080/api/users/getFolders", {params: paramf});
                let x = response.data.filter((item) => ((item !== "Trash") && (item !== "Sent") && (item !== params.folderName)))
                setFolders(x);
                setSelectedFolder(x[0])

                console.log(response.data)
            } catch (error) {
                console.error('Error fetching folders:', error);
            }
        }

        fetchMails();
        setSelectedFolder(Array(allMails.current.length).fill(false))
    }, [params.folderName, params.user]);


    async function moveFolder(e) {
        let ma =[];
        selectedRows.map((i) =>{
            ma = [...ma, mails.at(i).id]

        })
        console.log(ma);
        console.log(ma)
        if(params.folderName==="draft"){
            try {
                ma.map(async(x) => {
                    const r = await axios.post(`http://localhost:8080/api/users/deleteDraft/${x}/${params.user}`)
                    console.log(r.status);
                })
            } catch (error) {
                console.error('Error posting data:', error);
            }

        }
        else{
            try {
                const param = {
                    mailId: ma,
                    fromFolder: params.folderName,
                    toFolder: selectedFolder
                };

                console.log(param)

                const url =
                    `http://localhost:8080/api/users/moveFolder/${params.user}?mailId=${param.mailId}&fromFolder=${param.fromFolder}&toFolder=${e.target.id === "trash"?"Trash" :selectedFolder}`;

                await axios.post(url);
                window.location.reload()
            } catch (error) {
                console.error('Error moving folder:', error);
            }

        }


    }

    async function getPage(e){
        console.log(mail)

        const getPageParams = {
            page: parseInt(e.target.id,10),
            foldername: params.folderName,
            strategy: sortStrategy,
            isFiltered: isFiltered,
            filterType: filterMode,
            filterValue: filterValue,
        }
        console.log("value "+e.target.id)
        console.log(getPageParams)
        setCurrentPage(parseInt(e.target.id,10))

        const res = await axios.get(`http://localhost:8080/api/users/emailPages/${params.user}`,{params: getPageParams});
        setMails(res.data)
        console.log(res.data)
    }

    function search(e) {
        console.log(allMails.current);
        const searched = [...allMails.current].filter(mail => (mail.subject.toLowerCase().includes(e.target.value.toLowerCase()) || mail.sender.toLowerCase().includes(e.target.value.toLowerCase()) || mail.body.toLowerCase().includes(e.target.value.toLowerCase()) || mail.dateSent.toLowerCase().includes(e.target.value.toLowerCase())));
        console.log(searched)
        console.log(allMails.current.slice((currentPage-1)*2,currentPage*2))
        //setNumberOfPages(Array(res.data.length).fill(1))

        setMails(searched.slice((currentPage-1)*2,currentPage*2));
    }

    async function rename() {
        console.log(params.folderName)
        console.log(folderName)
        try{
            await axios.post(`http://localhost:8080/api/users/renameFolder/${params.user}/${params.folderName}/${folderName}`)
            setErrorMsg(false)

            navigate("/" + params.user + "/folder/" + folderName);
            window.location.reload();

        }catch (e) {
            setErrorMsg(true)
            setError("folder already exists")
        }



    }

    async function deleteFolder() {
        console.log(params.folderName)
        console.log(folderName)

        await axios.post(`http://localhost:8080/api/users/deleteFolder/${params.folderName}/${params.user}`)
        navigate("/" + params.user + "/folder/Inbox");
        window.location.reload()

    }

    function enableEdit() {
        return (<>
            <div className={"list-title"}>

                <div style={{display: "flex"}}><input className={"folder-name"} type={"text"} value={folderName}
                                                      onChange={(e) => setFolderName(e.target.value)}/>
                    <button className={"btn btn-lg"} style={{marginLeft: "20px"}} onClick={rename}><MdOutlineEdit
                        style={{fontSize: "2.5rem"}}/></button>
                </div>

                <button className={"btn btn-lg"} onClick={deleteFolder}><RiDeleteBin6Line style={{fontSize: "2.5rem"}}/>
                </button>

            </div>
            {errorMsg && <ErrorMsg message={error}/>}

        </>)
    }

    function disableEdit() {
        return (<div className={"list-title"}>
            <div style={{display: "flex", justifyContent: "space-between", width: "100%"}}>
                <h1 style={{fontSize: "3rem", fontWeight: "normal"}}>{folderName}</h1>
                <div>
                    <button className={"btn"} onClick={() => window.location.reload()}><SlReload/></button>
                </div>
            </div>
        </div>)

    }

    async function sort(e) {
        console.log(mails);
        console.log(e.target.value);
        setSortStrategies(e.target.value);
        const getPageParams = {
                page: 1,
                foldername: params.folderName,
                strategy: e.target.value,
                isFiltered: isFiltered,
                filterType: filterMode,
                filterValue: filterValue,
            }

            const res = await axios.get(`http://localhost:8080/api/users/emailPages/${params.user}`,{params: getPageParams});
            setMails(res.data)
            console.log(res.data)
            setCurrentPage(1)

    }

    async function filter(){
        if(!(filterValue ==="" && isFiltered === false)){
            const getPageParams = {
                page: 1,
                foldername: params.folderName,
                strategy: "",
                isFiltered: !isFiltered,
                filterType: filterMode,
                filterValue: filterValue,
            }

            const res = await axios.get(`http://localhost:8080/api/users/emailPages/${params.user}`,{params: getPageParams});
            setMails(res.data)
            setIsFiltered(!isFiltered);
            console.log(res.data)
            setNumberOfPages(Array(res.data.length+1).fill(1));
        }

    }


    const [selectedRows, setSelectedRows] = useState([]);
    const handleCheckboxChange = (index) => {
        setSelectedRows(prevSelectedRows => prevSelectedRows.includes(index) ? prevSelectedRows.filter(row => row !== index) : [...prevSelectedRows, index]);
        handleMoveFolder()
    };
    const handleMoveFolder = () => {
        // Implement your logic to move selected emails to the selected folder
        console.log("Selected rows: ", selectedRows);
    };

        return (
            <div className="list-box">

                {(folderName === "Inbox" || folderName === "Sent" || folderName === "Trash" || folderName === "draft") ? disableEdit() : enableEdit()}


                <hr/>
                <div className={"list-control"}>
                    <div>
                        <label>{"Sort by:  "}</label>
                        <select id="sort-dropdown" onChange={sort}>
                            <option value="date">Date</option>
                            <option value="subject">Subject</option>
                            <option value="body">Body</option>
                            <option value="importance">Importance</option>
                            <option value="sender">From</option>
                        </select>
                    </div>

                    <div>
                        <label><FiSearch style={{fontSize: "1.5rem"}}/></label>
                        <input className={"search-input"} type={"text"} placeholder={"search..."} onChange={search}/>
                    </div>
                    <div>
                        <lable>Filter by:</lable>
                        <select id={"filter-dropdown"} value={filterMode} onChange={(e) => setFilterMode(e.target.value)}>
                            <option value="sender">Sender</option>
                            <option value="subject">Subject</option>
                        </select>
                        <input type="text" value={filterValue} onChange={(e) => setFilterValue(e.target.value)}/>
                        <button className={"btn btn-sm"} onClick={filter}><IoMdCheckmark /></button>
                    </div>
                </div>


                <table className="table list-box-table table-hover">
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
                            <td><input value={index} type={"checkbox"} onChange={() => handleCheckboxChange(index)}/>
                            </td>
                            <td onClick={() => {
                                navigate("/" + params.user + "/" + (params.folderName === "draft" ? "draft/" : params.folderName + "/") + (mail.id === 0 ? ('draft/' + mail.temp) : mail.id));
                            }}>{mail.sender}</td>
                            <td onClick={() => {
                                navigate("/" + params.user + "/" + (params.folderName === "draft" ? "draft/" : params.folderName + "/") + (mail.id === 0 ? ('draft/' + mail.temp) : mail.id));
                            }}>{mail.subject}</td>
                            <td>{mail.dateSent}</td>
                            <td>{mail.priority}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
                <div style={{marginTop: "20px"}}>
                    {params.folderName !== "draft" &&<div>
                        <label>Move to: </label>
                        <select style={{marginRight: "10px", marginLeft: "10px"}}
                                value={selectedFolder}
                                onChange={(e) => {
                                    setSelectedFolder(e.target.value)
                                    console.log(selectedFolder)
                                }}
                        >
                            {folders.map((name, index) => (<option key={index} value={name}>{name}</option>))}

                        </select>
                        <button className={"btn btn-outline-dark btn-sm"} onClick={moveFolder}>Move</button>
                    </div>}


                    {params.folderName !== "Trash" && <button className={"btn trash"}
                                                              id={"trash"}
                                                              style={{marginLeft: "89%", marginRight: "20px"}}
                                                              onClick={moveFolder}

                    ><CiTrash id={"trash"} className={"trash"} style={{fontSize: "1.7rem"}}/></button>}
                </div>


                <nav aria-label="Page navigation example">
                {params.folderName !== "draft" && (
                    <ul className="pagination">
                    <li className="page-item">
                            <a className="page-link" href="#" aria-label="Previous">
                                <span aria-hidden="true">&laquo;</span>
                            </a>
                        </li>
                        {numberOfPages.map((i,index) => (
                            <li key={index+1} className={"page-item " +(index+1)} value={index+1} onClick={getPage}><a id={index+1} key={index+1} className="page-link" href="#" onClick={getPage}>{index+1}</a></li>))}
                        <li className="page-item">
                            <a className="page-link" href="#" aria-label="Next">
                                <span aria-hidden="true">&raquo;</span>
                            </a>
                        </li>
                    </ul>)}
                </nav>

            </div>
        );
}

export default ListBox;
