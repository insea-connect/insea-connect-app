import Placeholder from "@/components/placeholder";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { ArrowUp } from "lucide-react";

const AssistantPage = () => {
  return (
    <main className="flex flex-col w-full container mx-auto px-8 max-w-5xl">
      <div className="flex-1 " />
      <div className="py-8 relative">
        <Input
          type="search"
          placeholder="Send a message..."
          className="w-full bg-background h-14 pl-4 pr-8"
        />

        <Button className="absolute right-4 bottom-1/2 translate-y-1/2 h-9 w-9 px-2">
          <ArrowUp className="text-background" />
        </Button>
      </div>
    </main>
  );
};

export default AssistantPage;
