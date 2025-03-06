import { useEditor } from '@/stores/editor'
import { useToast } from '@/stores/toaststore'
import { useAppState } from '@/stores/appstate'

export const BACKEND_URL = import.meta.env.MODE === 'production' ? 'https://editor.firstdark.dev' : "http://localhost:3000";

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
       useToast().showToast(dt.message, 3000, 'error');
       return;
     }

     if (!isSocketConfig) {
       if (download) {
         useEditor().setDownloadConfig(true, dt.data)
         //downloadFile(dt.data);
       } else {
         useEditor().setTomlConfig(dt.data);
       }
       useToast().showToast('Success', 3000, 'success');
     } else {
       useToast().showToast('Config sent to server', 3000, 'success');
     }

   }).catch(err => {
     useToast().showToast(err, 3000, 'error');
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