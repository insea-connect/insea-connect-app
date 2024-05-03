import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Info, Paperclip, SendHorizonal } from "lucide-react";

const ChatArea = () => {
  return (
    <section
      aria-labelledby="primary-heading"
      className="bg-white flex h-full min-w-0 flex-1 flex-col overflow-y-auto lg:order-last"
    >
      <header className="h-14 w-full border-b py-2 px-4 flex items-center justify-between">
        <div className="flex items-center gap-2">
          <Avatar>
            <AvatarImage src="/avatar.jpg" alt="John Doe" />
            <AvatarFallback>JD</AvatarFallback>
          </Avatar>
          <span className="font-semibold">John Doe</span>
        </div>
        <Button
          variant="ghost"
          size="icon"
          className="rounded-lg group"
          aria-label="Info"
        >
          <Info className="w-5 h-5 text-muted-foreground group-hover:text-foreground" />
        </Button>
      </header>
      <div className="flex-1"></div>
      <div className="h-14 border-t flex py-2 px-4 gap-4 items-center">
        <Button
          variant="ghost"
          size="icon"
          className="rounded-lg group"
          aria-label="Info"
        >
          <Paperclip className="w-5 h-5 text-muted-foreground group-hover:text-foreground" />
        </Button>
        <Input
          type="text"
          placeholder="Type a message..."
          className="flex-1 bg-background px-4 py-2 rounded-md"
        />
        <Button size="icon" className="rounded-lg" aria-label="Info">
          <SendHorizonal className="w-5 h-5" />
        </Button>
      </div>
    </section>
  );
};

export default ChatArea;
