import {useEditor} from "../stores/editor.ts";

export const addToArray = (target: any, key: any) => {
    if (key === 'permissions') {
        const mcCmd = {
            role: "0",
            commands: [],
            permissionLevel: 1
        }

        target.push(mcCmd);
    }

    if (key === 'entry') {
      const overrideEntry = {
        ip: "127.0.0.1",
        presence: []
      }

      target.push(overrideEntry);
    }

    if (key === 'requiredRoles' || key === 'deniedRoles' || key === 'ignoredCommands' || key === 'ignoredThreads'
      || key === 'largeImageKey' || key === 'smallImageKey' || key === 'allowedChannels' || key === 'verifiedRole') {
        target.push('')
    }

    if (key === 'entries') {
        const filterEntry = useEditor().getConfig.config.general.configVersion > 26 ? {
            search: "",
            target: "CHAT",
            replace: "",
            searchMode: "CONTAINS",
            action: "IGNORE",
            appliesTo: "DISCORD",
            ignoreConsole: false
        } : {
            search: "",
            replace: "",
            searchMode: "CONTAINS",
            action: "IGNORE"
        }

        target.push(filterEntry);
    }

    if (key === 'buttons' && target.length < 2) {
        const mcButton = {
            label: "",
            url: ""
        }

        target.push(mcButton);
    }

    if (key === 'Buttons' && target.length < 2) {
        const stardewButton = {
            Title: "",
            Url: ""
        }

        target.push(stardewButton);
    }

    if (key === 'variables') {
        const mcVariable = {
            name: '',
            value: ''
        }

        target.push(mcVariable)
    }

    if (key === 'Variables') {
        const stardewVariable = {
            Name: '',
            Value: ''
        }

        target.push(stardewVariable)
    }

    if (key === 'dimensions') {
        if (useEditor().getConfig.config.general.version < 25) {
            const mcDim = {
                name: '',
                description: '',
                state: '',
                largeImageKey: [],
                largeImageText: '',
                smallImageKey: [],
                smallImageText: '',
                buttons: []
            }

            target.push(mcDim);
        } else {
            const mcDim = {
                name: '',
                presence: []
            }

            target.push(mcDim);
        }
    }

    if (key === 'roleAdded' || key === 'roleRemoved') {
        const roleAdded = {
            discordRole: '',
            minecraftCommand: []
        }

        target.push(roleAdded);
    }

    if (key === 'presence') {
        const presence = {
            type: "PLAYING",
            description: "",
            state: "",
            largeImageKey: [],
            largeImageText: "",
            smallImageKey: [],
            smallImageText: "",
            streamingActivityUrl: "https://twitch.tv/twitch",
            buttons: []
        }

        target.push(presence)
    }

    if (key === 'botStatus') {
        const botStatus = {
            status: '',
            botStatusType: 'CUSTOM_STATUS',
            botStatusStreamingURL: 'https://twitch.tv/twitch'
        }

        target.push(botStatus);
    }

    if (key === 'syncs') {
        const sync = {
            rank: '',
            role: ''
        }

        target.push(sync);
    }
}

export const headerToDisplay = (identifier: string) => {
    if (!identifier)
        return '';

    if (typeof identifier !== 'string')
      return '';

    const tempIdent = headerManual(identifier);

    if (tempIdent != identifier)
      return tempIdent;

    const words = identifier.match(/([A-Z]+(?=[A-Z][a-z])|[A-Z]?[a-z]+)/g) || [];
    // @ts-ignore
    const capitalizedWords = words.map(word => word.charAt(0).toUpperCase() + word.slice(1).toLowerCase());
    return capitalizedWords.join(' ');
}

const headerManual = (identifier: string) => {
  switch (identifier) {
    case 'ftbranks':
      return 'FTB Ranks';

    case 'ip':
      return "IP Address";

    case 'ftbessentials':
      return 'FTB Essentials';

    case 'cobblemonguilds':
      return 'Cobblemon Guilds';

    case 'ftbteams_chat':
      return 'FTB Teams Chat';

    case 'mcPrefix':
      return 'Minecraft Prefix';

    case 'mcReplyFormatting':
      return "Minecraft Reply Formatting"

    case 'playerroles':
      return 'Player Roles';

    case 'luckperms':
      return 'LuckPerms';

    case 'rpcImageServer':
      return 'RPC Image Server';

    case 'rpcImageServerUrl':
      return 'RPC Image Server URL';

    default:
      return identifier;
  }
}

export const isInvalidField = (value: unknown) => {
  const invalidFields = ['configVersion']
  return value != null && invalidFields.includes(value as string);
}

export const isStringArray = (value: unknown) => {
  const stringArrays = [
    'ignoredThreads',
    'ignoredCommands',
    'requiredRoles',
    'deniedRoles',
    'verifiedRole',
    'allowedChannels',
    'largeImageKey',
    'smallImageKey'
  ]

  return value != null && stringArrays.includes(value as string);
}

export const isEmptyOrNull = (value: unknown) => {
  return value == null || value === '' || typeof value === 'number';
}
