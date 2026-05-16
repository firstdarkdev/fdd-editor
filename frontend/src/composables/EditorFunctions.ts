import {useAppState} from "../stores/appstate.ts";
import {useEditor} from "../stores/editor.ts";

export const BACKEND_URL = import.meta.env.MODE === 'production' ? 'https://editor.firstdark.dev' : "http://localhost:3000";
const toast = useToast()

export const saveConfigFile = (download: boolean = false, isEmbed = false) => {
 if (isEmbed) {
   useEditor().setTomlConfig("");
 } else {
   const data = useEditor().getConfig
   const isSocketConfig = useEditor().checkIsSocketConfig;

   fetch(`${BACKEND_URL}/v1/saveconfig?isSocket=${isSocketConfig}&identifier=${useAppState().getIdentifier}`, {
     method: 'POST',
     headers: {
       'Content-Type': 'application/json'
     },
     body: JSON.stringify(data)
   }).then(async res => {
     const dt = await res.json();

     if (dt.error) {
       toast.add({
         title: "Error",
         description: dt.message,
         color: "error",
         duration: 2000
       })
       return;
     }

     if (!isSocketConfig) {
       if (download) {
         useEditor().setDownloadConfig(true, dt.data)
       } else {
         useEditor().setTomlConfig(dt.data);
       }
       toast.add({
         title: "Success",
         description: "Config saved",
         color: "success",
         duration: 2000
       })
     } else {
       toast.add({
         title: "Success",
         description: "Config sent to server",
         color: "success",
         duration: 2000
       })
     }

   }).catch(err => {
     toast.add({
       title: "Error",
       description: err,
       color: "error",
       duration: 2000
     })
   });
 }
}

export const downloadFile = (fileContent: any, isEmbed: boolean = false) => {
  const blob = new Blob([fileContent], { type: 'application/toml' });
  const link = document.createElement('a');

  link.href = URL.createObjectURL(blob);
  // @ts-ignore
  link.download = isEmbed ? "embed.json" : useEditor().getConfig.filename;
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
}
